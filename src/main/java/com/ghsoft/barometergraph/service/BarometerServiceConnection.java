package com.ghsoft.barometergraph.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by brian on 7/21/15.
 */
public class BarometerServiceConnection implements ServiceConnection {

    BarometerServiceEvents mEvents;

    public interface BarometerServiceEvents {
        public void onServiceConnect(BarometerDataService service);
        public void onServiceDisconnect();
    }

    public BarometerServiceConnection(BarometerServiceEvents events) {
        mEvents = events;
    }


    public void onServiceConnected(ComponentName className, IBinder service) {

        BarometerDataService.BarometerDataServiceBinder binder = (BarometerDataService.BarometerDataServiceBinder) service;
        mEvents.onServiceConnect(binder.getService());
    }

    public void onServiceDisconnected(ComponentName arg0) {
        mEvents.onServiceDisconnect();
    }

}
