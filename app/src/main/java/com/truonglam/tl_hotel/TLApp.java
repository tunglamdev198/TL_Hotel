package com.truonglam.tl_hotel;

import android.app.Application;

public class TLApp extends Application {
    private static TLApp instance;

    public static TLApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
