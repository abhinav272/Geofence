package com.abhinav.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinav.sharma on 9/26/2016.
 */
public class HomeActivity extends AppCompatActivity {

    private static final String HOME_GEOFENCE = "A-97,Sector 55, NOIDA";
    private static final String GIP = "GIP Mall Noida";
    public static final String GEOFENCING_REQUEST = "geoFencingRequest";
    private Button startLocationMonitoring, startGeofencingMonitoring, stopGeofencingMonitoring, stopLocationMonitoring, showMap;
    private GoogleApiClient apiClient = null;
    private Intent mapIntent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (response != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        apiClient.disconnect();
    }

    private void setupUI() {

        startLocationMonitoring = (Button) findViewById(R.id.btn1);
        startGeofencingMonitoring = (Button) findViewById(R.id.btn2);
        stopGeofencingMonitoring = (Button) findViewById(R.id.btn3);
        stopLocationMonitoring = (Button) findViewById(R.id.btn4);
        showMap = (Button) findViewById(R.id.btn5);


        startLocationMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationRequest locationRequest = LocationRequest.create()
                        .setInterval(5000)
                        .setFastestInterval(5000)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 272);
                    }
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.e("onLocationChanged: ", "Location is: " + location.toString());
                    }
                });
            }
        });

        startGeofencingMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geofence geofence = new Geofence.Builder()
                        .setRequestId(HOME_GEOFENCE)
                        .setCircularRegion(28.6071506, 77.3466258, 100.0f)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setNotificationResponsiveness(1000)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                        .setLoiteringDelay(5000)
                        .build();

                Geofence geofenceGIP = new Geofence.Builder()
                        .setRequestId(GIP)
                        .setCircularRegion(28.567706, 77.326010, 100.0f)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setNotificationResponsiveness(1000)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                        .setLoiteringDelay(5000)
                        .build();

                GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
                        .addGeofence(geofence)
                        .addGeofence(geofenceGIP).build();
                Intent intent = new Intent(HomeActivity.this, MyService.class);
                PendingIntent pendingIntent = PendingIntent.getService(HomeActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                GeofenceModel.List list = new GeofenceModel.List();
                list.add(new GeofenceModel(28.6071506, 77.3466258, 100f, HOME_GEOFENCE));
                list.add(new GeofenceModel(28.567706, 77.326010, 100f, GIP));

                mapIntent = new Intent(HomeActivity.this, MapActivity.class);
                mapIntent.putParcelableArrayListExtra(GEOFENCING_REQUEST, list);

                if (apiClient.isConnected()) {
                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 272);
                        }
                        return;
                    }
                    LocationServices.GeofencingApi.addGeofences(apiClient, geofencingRequest, pendingIntent)
                            .setResultCallback(new ResultCallbacks<Status>() {
                                @Override
                                public void onSuccess(@NonNull Status status) {
                                    Log.e("onSuccess: ", "GeofencingRequest added successfully");
                                }

                                @Override
                                public void onFailure(@NonNull Status status) {
                                    Log.e("onFailure: ", "GeofencingRequest not added");
                                }
                            });
                }
            }
        });

        stopGeofencingMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick: ", "Geofencing monitoring stopped");
                List<String> geofences = new ArrayList<String>();
                geofences.add(HOME_GEOFENCE);
                geofences.add(GIP);
                if(apiClient.isConnected())
                    LocationServices.GeofencingApi.removeGeofences(apiClient, geofences);
            }
        });

        stopLocationMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiClient.disconnect();
                Toast.makeText(HomeActivity.this, "Location Monitoring Stopped", Toast.LENGTH_SHORT).show();
            }
        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapIntent != null) {
                    startActivity(mapIntent);
                }
            }
        });

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.e("GoogleApiClient", "onConnected: ");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.e("GoogleApiClient", "onConnectionSuspended: ");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e("GoogleApiClient", "onConnectionFailed: ");
                    }
                })
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 272);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // TODO -- use case for permission not given is not handled yet .. Be careful !!
        if(requestCode == 272
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }
}
