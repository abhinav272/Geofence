package com.abhinav.geofence;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by abhinav.sharma on 9/27/2016.
 */
public class MyService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyService(String name) {
        super(MyService.class.getSimpleName());
    }

    public MyService() {
        super(MyService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e("onHandleIntent: ", geofencingEvent.getErrorCode() + "");
        } else {
            int transition = geofencingEvent.getGeofenceTransition();
            String requestId = geofencingEvent.getTriggeringGeofences().get(0).getRequestId();
            sendNotification(transition, requestId);
            switch (transition) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    Log.e("onHandleIntent: ", "Enter transition for " + requestId);
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    Log.e("onHandleIntent: ", "Exit transition for " + requestId);
                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    Log.e("onHandleIntent: ", "Dwell transition for " + requestId);
                    break;
            }
        }
    }

    private void sendNotification(int transition, String requestId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.location_icon_2_svg)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{100, 100, 100, 100})
                .setAutoCancel(true);

        switch (transition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                mBuilder.setContentTitle("Welcome to " + requestId)
                        .setContentText("You are most welcome to " + requestId);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                mBuilder.setContentTitle("Thanks for visiting " + requestId)
                        .setContentText("Do visit again to " + requestId);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                mBuilder.setContentTitle("Explore " + requestId)
                        .setContentText("Make your self comfortable at " + requestId);
                break;
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(transition, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("IntentService", "onDestroy: ");

    }
}
