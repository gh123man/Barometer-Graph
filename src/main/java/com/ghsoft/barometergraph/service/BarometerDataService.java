package com.ghsoft.barometergraph.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by brian on 7/21/15.
 */
public class BarometerDataService extends Service {

    private final BarometerDataServiceBinder mBinder = new BarometerDataServiceBinder();
    public int test;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("test", "started");
        test = 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public class BarometerDataServiceBinder extends Binder {
        public BarometerDataService getService() {
            return BarometerDataService.this;
        }
    }
}
