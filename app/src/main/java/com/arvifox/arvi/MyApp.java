package com.arvifox.arvi;

import android.app.Application;

/**
 * Created by
 */

public class MyApp extends Application {
//    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

//        mNetComponent = DaggerNetComponent.builder()
//                 list of modules that are part of this component need to be created here too
//                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
//                .netModule(new NetModule(1))
//                .cjeModule(new CjeModule())
//                .build();
    }

//    public NetComponent getNetComponent() {
//        return mNetComponent;
//    }
}
