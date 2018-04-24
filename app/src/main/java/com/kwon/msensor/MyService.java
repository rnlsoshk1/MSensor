package com.kwon.msensor;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class MyService  extends Service implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor mSensor_Magnetic;
    double[] mMagnetic = new double[3];

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor_Magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager.registerListener(this, mSensor_Magnetic, SensorManager.SENSOR_DELAY_UI);
        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagnetic[0] = event.values[0];
            mMagnetic[1] = event.values[1];
            mMagnetic[2] = event.values[2];

            Log.i("service", mMagnetic[0] + ", " + mMagnetic[1] + ", " + mMagnetic[2]);

            Intent myFilteredResponse = new Intent("com.kwon.msensor.step");
            myFilteredResponse.putExtra("serviceData", mMagnetic);

            sendBroadcast(myFilteredResponse);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("LOG", "onDestroy()");
        mSensorManager.unregisterListener(this);
    }
}