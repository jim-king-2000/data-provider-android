package com.bytebilling.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import io.sgr.geometry.Coordinate;
import io.sgr.geometry.utils.GeometryUtils;

class DataProviderGPS {
    public DataProviderGPS(Context context, long minTime, float minDistance, final DataProviderCallback callback) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                callback.accept(locationToJson(location));
                Log.d("sensor", "GPS");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
    }

    public void onDestroy() {
        mLocationManager.removeUpdates(mLocationListener);
    }

    private JSONObject locationToJson(Location location) {
        try {
            JSONObject gps = new JSONObject();
            gps.put("timestamp", location.getTime());

            JSONObject loc = new JSONObject();
            Coordinate mars = GeometryUtils.wgs2gcj(
                new Coordinate(location.getLatitude(), location.getLongitude()));
            loc.put("latitude", mars.getLat());
            loc.put("longitude", mars.getLng());
            loc.put("altitude", location.getAltitude());
            loc.put("accuracy", location.getAccuracy());
            loc.put("speed", location.getSpeed());
            loc.put("heading", location.getBearing());
            gps.put("location", loc);
            return gps;
        } catch (Exception e) {
            Log.e("DataProvider", e.toString());
            return null;
        }
    }

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
}

