package com.ghsoft.barometergraph.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import com.ghsoft.barometergraph.data.BarometerDataPoint;
import com.ghsoft.barometergraph.data.IDataReceiver;

import java.util.LinkedList;

/**
 * Created by brian on 7/21/15.
 */
public class BarometerDataService extends Service implements SensorEventListener {

    private static final int BUFFER_CAP = 2000;
    private final BarometerDataServiceBinder mBinder = new BarometerDataServiceBinder();
    private IDataReceiver mDataReceiver;
    private LinkedList<BarometerDataPoint> mBuffer;
    private SensorManager mSensorManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mBuffer = new LinkedList<>();
    }

    public void setDataReceiver(IDataReceiver receiver) {
        mDataReceiver = receiver;
        mDataReceiver.writeHistory(mBuffer);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void stopAndClose() {
        mSensorManager.unregisterListener(this);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        BarometerDataPoint point = new BarometerDataPoint(event.values[0], event.timestamp);

        mBuffer.add(point);
        if (mDataReceiver != null) {
            //Log.e(System.identityHashCode(this) + " ", "" + mBuffer.size());
            mDataReceiver.write(point);
        }

        if (mBuffer.size() > BUFFER_CAP) {
            mBuffer.removeLast();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class BarometerDataServiceBinder extends Binder {
        public BarometerDataService getService() {
            return BarometerDataService.this;
        }
    }
}
