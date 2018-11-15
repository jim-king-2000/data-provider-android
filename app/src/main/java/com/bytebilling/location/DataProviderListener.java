package com.bytebilling.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DataProviderListener {
    private DataProviderListener() {
    }

    public static void registerListener(Context context, BroadcastReceiver listener) {
        LocalBroadcastManager.getInstance(context).registerReceiver(
            listener,
            new IntentFilter("DATA_PROVIDER"));
    }
}
