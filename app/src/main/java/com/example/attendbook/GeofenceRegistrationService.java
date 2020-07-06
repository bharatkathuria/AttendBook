package com.example.attendbook;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceRegistrationService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private static boolean statusIsInside = false;
    private static final String TAG = "MainActivity";


    public GeofenceRegistrationService() {

        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG, "Geofence Service Status:");

        GeofencingEvent geofencingEvent  = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){

            Log.d(TAG, "GeofencingEvent error " + geofencingEvent.getErrorCode());
        }
        else{

            int transaction = geofencingEvent.getGeofenceTransition();
            List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
            Geofence geofence = geofences.get(0);
            GeoFenceServiceStatus geoFenceServiceStatus = GeoFenceServiceStatus.getInstance();

            if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER && geofence.getRequestId().equals("IP_UNI")) {
                statusIsInside = true;
                Log.d(TAG, "You are inside GGSIPU");
            } else {
                statusIsInside = false;
                Log.d(TAG, "You are outside GGSIPU");
            }
            geoFenceServiceStatus.setInside(statusIsInside);

        }
    }

}
