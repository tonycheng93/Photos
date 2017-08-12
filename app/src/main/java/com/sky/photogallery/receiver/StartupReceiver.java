package com.sky.photogallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartupReceiver extends BroadcastReceiver {

    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received broadcast intent = " + intent.getAction());
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
