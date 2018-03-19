package com.user.planb;

/**
 * Created by user on 1/30/2018.
 */

import android.app.Application;

import com.user.planb.receiver.ConnectivityReceiver;

public class ConnectivityStarter extends Application {

    private static ConnectivityStarter mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized ConnectivityStarter getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}