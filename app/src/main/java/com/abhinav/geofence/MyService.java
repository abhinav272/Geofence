package com.abhinav.geofence;

import android.app.IntentService;
import android.content.Intent;
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
        if(geofencingEvent.hasError()){
            Log.e("onHandleIntent: ", geofencingEvent.getErrorCode()+"");
        } else {
            int transition = geofencingEvent.getGeofenceTransition();
            String requestId = geofencingEvent.getTriggeringGeofences().get(0).getRequestId();
            switch (transition){
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    Log.e("onHandleIntent: ", "Enter transition for "+ requestId);
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    Log.e("onHandleIntent: ", "Exit transition for "+ requestId);
                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    Log.e("onHandleIntent: ", "Dwell transition for "+ requestId);
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("IntentService", "onDestroy: ");

    }
}
