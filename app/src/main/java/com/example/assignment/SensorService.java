package com.example.assignment;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SensorService extends Service implements SensorEventListener {

    private static final String TAG = "SensorService";

    private Sensor sensor;
    private SensorManager sensorManager;

            //      Service Functions       //

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Service Binded", Toast.LENGTH_SHORT).show();
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Ended", Toast.LENGTH_SHORT).show();
    }

            //      Sensor      //

    @Override
    public void onSensorChanged(SensorEvent event) {
        // grab the values and timestamp -- off the main thread
        new SensorEventTask().execute(event);
        // stop the service
        stopSelf();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

            //      Async Task      //

    static class SensorEventTask extends AsyncTask<SensorEvent,Float, Float> {

        @Override
        protected void onProgressUpdate(Float... progress) {
            Log.i(TAG, Float.toString(progress[0]));
        }

        @Override
        protected Float doInBackground(SensorEvent... events) {
            publishProgress(events[0].values[0]);

            return null;
        }
    }

    private final IBinder binder = new NotificationBinder();

    class NotificationBinder extends Binder {
        SensorService getService() {
            return SensorService.this;
        }
    }


}
