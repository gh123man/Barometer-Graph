package com.ghsoft.barometergraph.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by brian on 7/21/15.
 */
public class BarometerServiceConnection implements ServiceConnection {

    private BarometerServiceEvents mEvents;
    private boolean mBound;

    public interface BarometerServiceEvents {
        void onServiceConnect(BarometerDataService service);
        void onServiceDisconnect();
    }

    public BarometerServiceConnection(BarometerServiceEvents events) {
        mEvents = events;
        mBound = false;
    }

    public void onServiceConnected(ComponentName className, IBinder service) {

        BarometerDataService.BarometerDataServiceBinder binder = (BarometerDataService.BarometerDataServiceBinder) service;
        mEvents.onServiceConnect(binder.getService());
        mBound = true;
    }

    public void onServiceDisconnected(ComponentName arg0) {
        if (mBound) {
            mBound = false;
            mEvents.onServiceDisconnect();
        }
    }
    public boolean isBound() {
        return mBound;
    }

}
