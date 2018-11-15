package com.bytebilling.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.json.JSONObject;

class DataProviderSensor {
    public DataProviderSensor(Context context, final DataProviderCallback callback) {
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                try {
                    if (Sensor.TYPE_LIGHT == event.sensor.getType()) {
                        JSONObject light = new JSONObject();
                        light.put("light", event.values[0]);
                        callback.accept(light);
                        Log.d("sensor", "light");
                    } else if (Sensor.TYPE_LINEAR_ACCELERATION == event.sensor.getType()) {
                        JSONObject values = new JSONObject();
                        values.put("x", event.values[0]);
                        values.put("y", event.values[1]);
                        values.put("z", event.values[2]);
                        JSONObject acceleration = new JSONObject();
                        acceleration.put("acceleration", values);
                        callback.accept(acceleration);
                        Log.d("sensor", "acceleration");
                    }
                    assert(false);
                } catch (Exception e) {
                    Log.e("sensor", e.toString());
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (null != sensor)
            mSensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (null != sensor)
            mSensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onDestroy() {
        mSensorManager.unregisterListener(listener);
    }

    private SensorManager mSensorManager;
    private SensorEventListener listener;
}
