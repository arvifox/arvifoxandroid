package com.arvifox.arvi

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import okio.Path.Companion.toOkioPath

/**
 * Created by
 */
class MyApp : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        //        Configuration configuration = new Configuration.Builder()
//                .setMinimumLoggingLevel(Log.VERBOSE)
//                .build();
//
//        WorkManager.initialize(this, configuration);

//        mNetComponent = DaggerNetComponent.builder()
//                 list of modules that are part of this component need to be created here too
//                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
//                .netModule(new NetModule(1))
//                .cjeModule(new CjeModule())
//                .build();
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.1)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.03)
                    .directory(cacheDir.toOkioPath())
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }

    override fun onTerminate() {
        Log.d("foxx", "appl on terminate")
        super.onTerminate()
    } //    public NetComponent getNetComponent() {
    //        return mNetComponent;
    //    }

    companion object {
        //    private NetComponent mNetComponent;
        private val POWERMANAGER_INTENTS = arrayOf(
            Intent().setComponent(
                ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.letv.android.letvsafe",
                    "com.letv.android.letvsafe.AutobootManageActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.startupapp.StartupAppListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.oppo.safe",
                    "com.oppo.safe.permission.startup.StartupAppListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.samsung.android.lool",
                    "com.samsung.android.sm.ui.battery.BatteryActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.htc.pitroad",
                    "com.htc.pitroad.landingpage.activity.LandingPageActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.asus.mobilemanager",
                    "com.asus.mobilemanager.MainActivity"
                )
            )
        )
    }
}
