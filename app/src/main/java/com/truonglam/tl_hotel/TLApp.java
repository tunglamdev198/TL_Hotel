package com.truonglam.tl_hotel;

import android.app.Application;

import com.truonglam.tl_hotel.receiver.NetworkChangedReceiver;

public class TLApp extends Application {
    private static TLApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized TLApp getInstance() {
        return instance;
    }

    public void setConnectivityListener(NetworkChangedReceiver.ConnectivityReceiverListener listener) {
        NetworkChangedReceiver.connectivityReceiverListener = listener;
    }
}
