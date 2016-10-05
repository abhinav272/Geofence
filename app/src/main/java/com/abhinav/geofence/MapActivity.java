package com.abhinav.geofence;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinav.sharma on 10/3/2016.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Todo code for map showing and geo-fence plotting
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 272);
            }
            return;
        }
//        LatLng sydney = new LatLng(-33.867, 151.206);
        List<GeofenceModel> allGeofences = getIntent().getParcelableArrayListExtra(HomeActivity.GEOFENCING_REQUEST);

        for (GeofenceModel geofence : allGeofences) {

            googleMap.addMarker(new MarkerOptions()
            .title(geofence.getId())
            .position(new LatLng(geofence.getLatitude(), geofence.getLongitude()))
            .snippet(geofence.getRadius() + "Radius for this geofence"));

            CircleOptions circleOptions = new CircleOptions()
                    .center( new LatLng(geofence.getLatitude(), geofence.getLongitude()) )
                    .radius(geofence.getRadius())
                    .fillColor(0x40ff0000)
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(2);

            googleMap.addCircle(circleOptions);
        }


        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(allGeofences.get(0).getLatitude(), allGeofences.get(0).getLongitude()), 15));

//        googleMap.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
//
//        CircleOptions circleOptions = new CircleOptions()
//                .center( new LatLng(sydney.latitude, sydney.longitude) )
//                .radius(100)
//                .fillColor(0x40ff0000)
//                .strokeColor(Color.TRANSPARENT)
//                .strokeWidth(2);
//
//        googleMap.addCircle(circleOptions);
    }
}
