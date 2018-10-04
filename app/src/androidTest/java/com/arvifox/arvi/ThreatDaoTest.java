package com.arvifox.arvi;

import android.arch.persistence.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.arvifox.arvi.roomexample.FileThreat;
import com.arvifox.arvi.roomexample.IThreatDao;
import com.arvifox.arvi.roomexample.LocalSeverityLevel;
import com.arvifox.arvi.roomexample.LocalSuspiciousThreatType;
import com.arvifox.arvi.roomexample.LocalThreatInfo;
import com.arvifox.arvi.roomexample.LocalThreatType;
import com.arvifox.arvi.roomexample.db.ThreatDb;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * тесты на {@link IThreatDao}
 *
 * @author Alisov AY
 * @since 23.01.2018
 */
@RunWith(AndroidJUnit4.class)
public class ThreatDaoTest {

    private ThreatDb mThreatDb;
    private IThreatDao mThreatDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        mThreatDb = Room
                .inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), ThreatDb.class)
                .allowMainThreadQueries()
                .build();
        mThreatDao = mThreatDb.threatDao();
    }

    @After
    public void closeDb() throws IOException {
        mThreatDb.close();
    }

    //region tests

    /**
     * обновляет угрозу и проверяет что она действительно обновилась
     */
    @Test
    public void updateAndGetThreats() {
        String newPackageName = "test.new.package.name";
        FileThreat fileThreat = createThreat();
        mThreatDao.saveThreat(fileThreat);
        FileThreat newFileThreat = createThreat();
        newFileThreat.getThreatInfo().setPackageName(newPackageName);
        newFileThreat.setId(1);
        mThreatDao.updateThreat(newFileThreat);
        mThreatDao.getAllThreats().test()
                .assertValue(fileThreats -> fileThreats.size() == 1
                        && fileThreats.get(0).getThreatInfo().getPackageName().equalsIgnoreCase(newPackageName));
    }

    /**
     * вставляет угрозу и проверяет что она успешно сохранилась
     */
    @Test
    public void insertAndGetThreat() {
        FileThreat fileThreat = createThreat();
        mThreatDao.saveThreat(fileThreat);
        mThreatDao.getAllThreats().test()
                .assertValue(fileThreats -> fileThreats.size() == 1
                        && fileThreats.get(0).equals(fileThreat));
    }

    /**
     * проверяет что в пустой базе нет угроз
     */
    @Test
    public void getThreatsWhenNoThreatInDb() {
        mThreatDao.getAllThreats().test()
                .assertNever(fileThreats -> fileThreats != null && fileThreats.size() > 0)
                .assertComplete();
    }

    /**
     * удаляет все угрозы и проверяет что их реально нет
     */
    @Test
    public void removeAllAndGetThreats() {
        FileThreat fileThreat = createThreat();
        mThreatDao.saveThreat(fileThreat);
        mThreatDao.removeAllThreats();
        mThreatDao.getAllThreats().test()
                .assertNever(fileThreats -> fileThreats != null && fileThreats.size() > 0)
                .assertComplete();
    }

    //endregion

    //region private

    /**
     * Создаёт тестовый объект FileThreat
     *
     * @return объект FileThreat
     */
    private FileThreat createThreat() {
        return createThreat("testVirus", "testObject", "some/test/path",
                true, "test.package.name", false,
                LocalSeverityLevel.MEDIUM, false);
    }

    /**
     * Создаёт тестовый объект FileThreat
     *
     * @return объект FileThreat с тестовыми данными
     */
    private FileThreat createThreat(String virusName, String objectName, String fileFullPath,
                                    boolean application, String packageName, boolean isCloudCheckFailed,
                                    LocalSeverityLevel severityLevel, boolean ignored) {
        final LocalThreatInfo threatInfo =
                new LocalThreatInfo(virusName, objectName, fileFullPath,
                        application, packageName, isCloudCheckFailed, severityLevel);
        FileThreat threat;
        threat = new FileThreat(threatInfo, LocalThreatType.MALWARE,
                LocalSuspiciousThreatType.BANK_SMS, 0, false);
        threat.setIgnored(ignored);
        return threat;
    }

    //endregion
}
