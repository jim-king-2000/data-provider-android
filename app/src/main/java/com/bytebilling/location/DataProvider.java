package com.bytebilling.location;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DataProvider implements DataProviderCallback {
    public DataProvider(Context context, long minTime, float minDistance) {
        mContext = context;
        mDataProviderSensor = new DataProviderSensor(context, this);
        mDataProviderGPS = new DataProviderGPS(context, minTime, minDistance, this);
    }

    public void register(DataProviderInterface provider) {
        mDataProvider3Party.add(provider);
    }

    public void onDestroy() {
        if (null != mDataProviderGPS) mDataProviderGPS.onDestroy();
        if (null != mDataProviderSensor) mDataProviderSensor.onDestroy();
    }

    @Override
    public void accept(JSONObject param) {
        if (param.has("timestamp")) {
            broadcastIntent(mContext, mergeGPSJson(param).toString());
            return;
        }

        mergeJson(param);
        for (DataProviderInterface provider : mDataProvider3Party) {
            try {
                mergeJson(provider.getData());
            } catch (Exception e) {
                Log.e("DataProvider", e.toString());
            }
        }
        Log.d("DataProvider", mData.toString());
    }

    private JSONObject mergeGPSJson(JSONObject param) {
        try {
            JSONObject merged = new JSONObject(param.toString());
            merged.put("sensors", mData);
            return merged;
        } catch (Exception e) {
            Log.e("DataProvider", e.toString());
            return new JSONObject();
        }
    }

    private void mergeJson(JSONObject param) {
        try {
            Iterator<String> keys = param.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                mData.put(key, param.opt(key));
            }
        } catch (Exception e) {
            Log.e("DataProvider", e.toString());
        }
    }

    private void broadcastIntent(Context context, String data)
    {
        Intent intent = new Intent("DATA_PROVIDER");
        intent.putExtra("Data", data);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private Context mContext;
    private DataProviderSensor mDataProviderSensor;
    private DataProviderGPS mDataProviderGPS;
    private JSONObject mData = new JSONObject();
    private ArrayList<DataProviderInterface> mDataProvider3Party = new ArrayList<>();
}
