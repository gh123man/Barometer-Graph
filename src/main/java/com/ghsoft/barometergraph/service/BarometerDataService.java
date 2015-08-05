package com.ghsoft.barometergraph.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ghsoft.barometergraph.data.BarometerDataPoint;
import com.ghsoft.barometergraph.data.CSVWriter;
import com.ghsoft.barometergraph.data.FileMan;
import com.ghsoft.barometergraph.data.IDataReceiver;
import com.ghsoft.barometergraph.data.ISettingsProvider;
import com.ghsoft.barometergraph.data.RunningAverage;
import com.ghsoft.barometergraph.data.TransformHelper;
import com.ghsoft.barometergraph.views.BarometerDataGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Created by brian on 7/21/15.
 */
public class BarometerDataService extends Service implements SensorEventListener {

    private static final int BUFFER_CAP = 5000;
    private final BarometerDataServiceBinder mBinder = new BarometerDataServiceBinder();
    private IDataReceiver mDataReceiver;
    private LinkedList<BarometerDataPoint> mBuffer;
    private SensorManager mSensorManager;
    private RunningAverage mAverager;
    private BarometerDataGraph.TransformFunction mTransform;
    private FileMan mFileMan;
    private CSVWriter mWriter;
    private File mFile;
    private boolean mRecording, mHasSensor;
    private ISettingsProvider mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mHasSensor = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mFileMan = new FileMan();
        mBuffer = new LinkedList<>();
        mWriter = new CSVWriter();
        mFile = null;
        mRecording = false;
    }

    public boolean hasSensor() {
        return mHasSensor;
    }

    public void setSettings(ISettingsProvider settings) {
        mSettings = settings;
        mAverager = new RunningAverage(mSettings.getAverageSize());
        mTransform = TransformHelper.fromUnit(mSettings.getUnit());
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
        cleanup();
    }

    private void cleanup() {
        if (mRecording) {
            mWriter.finish();
        }
        mFileMan.clearTmp();
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
        if (!hasSensor()) return;
        mAverager.put(event.values[0]);
        BarometerDataPoint point = new BarometerDataPoint(getCorrectValue(mAverager.get()), System.currentTimeMillis());
        mBuffer.add(point);
        if (mDataReceiver != null) {
            //Log.e(System.identityHashCode(this) + " ", "" + mBuffer.size());
            mDataReceiver.write(point);
        }

        if (mRecording) {
            mWriter.writeRow(new String[] {"" + point.getmTimeStamp(), "" + point.getValue()});
        }

        if (mBuffer.size() >= BUFFER_CAP) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        cleanup();
    }

    public void setRunningAverageSize(int size) {
        if (mRecording) return;
        mSettings.setAverageSize(size);
        mAverager.setSize(size);
    }

    public int getAverageSize() {
        return mAverager.getSize();
    }

    public void setTransformFunction(BarometerDataGraph.TransformFunction transform) {
        mSettings.setUnit(transform.getUnit());
        mTransform = transform;
    }

    public void clear() {
        mBuffer.clear();
        mAverager.clear();
    }

    public String getUnit() {
        if (mTransform != null) return mTransform.getUnit();
        return "";
    }

    private float getCorrectValue(float value) {
        if (mTransform != null) return mTransform.transform(value);
        return value;
    }

    public void startRecording(boolean saveBuffer) {
        mFile = mFileMan.acquireTempFile();
        try {
            mWriter.setWriter(new PrintWriter(mFile));


            if (!saveBuffer) {
                mRecording = true;
            } else {
                for (BarometerDataPoint p : mBuffer) {
                    mWriter.writeRow(new String[] {"" + p.getmTimeStamp(), "" + p.getValue()});
                }
                mRecording = true;
            }
        } catch (FileNotFoundException e) {
            //fail
        }
    }

    public String stopRecording() {
        mRecording = false;
        mWriter.finish();
        return mFile.getName();
    }

    public void finalizeRecording(String newFileName) {
        mFileMan.rename(newFileName, mFile);
        Log.e("moving " + newFileName, "moving" + mFile.getName());
        mFile = null;
    }

    public boolean isRecording() {
        return mRecording;
    }

}
