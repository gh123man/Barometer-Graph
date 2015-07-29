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
import com.ghsoft.barometergraph.data.RunningAverage;

import java.util.LinkedList;

/**
 * Created by brian on 7/21/15.
 */
public class BarometerDataService extends Service implements SensorEventListener {

//    private static final int BUFFER_CAP = Integer.MAX_VALUE;
    private final BarometerDataServiceBinder mBinder = new BarometerDataServiceBinder();
    private IDataReceiver mDataReceiver;
    private LinkedList<BarometerDataPoint> mBuffer;
    private SensorManager mSensorManager;
    private RunningAverage mAverager;

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mBuffer = new LinkedList<>();
        mAverager = new RunningAverage(10);
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
            stopAndClose();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mAverager.put(event.values[0]);
        BarometerDataPoint point = new BarometerDataPoint(mAverager.get(), System.currentTimeMillis());
        mBuffer.add(point);
        if (mDataReceiver != null) {
            //Log.e(System.identityHashCode(this) + " ", "" + mBuffer.size());
            mDataReceiver.write(point);
        }

//        if (mBuffer.size() >= BUFFER_CAP) {
//            mBuffer.removeLast();
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class BarometerDataServiceBinder extends Binder {
        public BarometerDataService getService() {
            return BarometerDataService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    public void setRunningAverageSize(int size) {
        mAverager.setSize(size);
    }

    public int getAverageSize() {
        return mAverager.getSize();
    }
}
