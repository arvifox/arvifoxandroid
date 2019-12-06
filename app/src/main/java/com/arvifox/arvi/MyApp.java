package com.arvifox.arvi;

import android.app.Application;
import android.util.Log;

import androidx.multidex.MultiDexApplication;
import androidx.work.Configuration;
import androidx.work.WorkManager;

/**
 * Created by
 */

public class MyApp extends MultiDexApplication {
//    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Configuration configuration = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.VERBOSE)
                .build();

        WorkManager.initialize(this, configuration);

//        mNetComponent = DaggerNetComponent.builder()
//                 list of modules that are part of this component need to be created here too
//                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
//                .netModule(new NetModule(1))
//                .cjeModule(new CjeModule())
//                .build();
    }

    @Override
    public void onTerminate() {
        Log.d("foxx", "appl on terminate");
        super.onTerminate();
    }

    //    public NetComponent getNetComponent() {
//        return mNetComponent;
//    }
}
