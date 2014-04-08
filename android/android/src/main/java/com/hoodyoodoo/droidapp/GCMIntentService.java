package com.hoodyoodoo.droidapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.fatfractal.ffef.FFException;
import com.google.android.gcm.GCMBaseIntentService;
import com.hoodyoodoo.droidapp.activity.HoodyoodooActivity;

public class GCMIntentService extends GCMBaseIntentService
{
    /**
     * The GCMIntentService class must create a no-arg constructor and pass the
     * sender id to be used for registration.
     */
    public GCMIntentService() {
        super(Hoodyoodoo.GCM_SENDER_ID);
    }

    @Override
    public void onRegistered(Context context, String regId) {
        // save regId to FF backend
        Log.i(this.getClass().getName(), "Successfully registered for C2DM! regId = " + regId);
        try {
            Hoodyoodoo.getFF().registerNotificationID(regId);
        }
        catch (FFException e) {
            Log.e(this.getClass().getName(), "Failed to register notification ID with FatFractal backend!");
            e.printStackTrace();
        }
    }

    @Override
    public void onUnregistered(Context context, String regId) {
        // remove registrationId from FF backend
        Log.i(this.getClass().getName(), "Successfully unregistered for C2DM! regId = " + regId);
        try {
            Hoodyoodoo.getFF().unregisterNotificationID();
        }
        catch (FFException e) {
            Log.e(this.getClass().getName(), "Failed to unregister notification ID with FatFractal backend!");
            e.printStackTrace();
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String payloadMsg = extras.getString("payload");
            Log.i(this.getClass().getName(), "Received message : " + payloadMsg);

            Notification.Builder notification = new Notification.Builder(context);
            notification.setContentTitle("Been outdone!");
            notification.setTicker("Been outdone!");
            notification.setContentText(payloadMsg);
            notification.setSmallIcon(R.drawable.ic_launcher);
            notification.setAutoCancel(true);

            Intent launchIntent = new Intent(this, HoodyoodooActivity.class);
            launchIntent.putExtra(HoodyoodooActivity.EXTRA_TAB_TAG, HoodyoodooActivity.TAB_TOP_CELEB_TAG);
            launchIntent.setAction(Intent.ACTION_VIEW); // necessary for extras to be passed on to Action
            Log.i(getClass().getName(), "Sending intent : " + launchIntent);
            notification.setContentIntent(PendingIntent.getActivity(context, 0, launchIntent, 0));

            NotificationManager nm = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, notification.getNotification());
        }
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.e(this.getClass().getName(), "GCM error : " + errorId);
    }
}
