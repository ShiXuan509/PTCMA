package com.project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class adminLocationManagement extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleMap map;
    FloatingActionButton fab;
    EditText locSearch;
    ImageView searchIcon;
    DatabaseReference firebase;
    LatLng defaultLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location_management);

        fab = findViewById(R.id.fab);
        locSearch = findViewById(R.id.et_search);
        searchIcon = findViewById(R.id.search_icon);
        firebase = FirebaseDatabase.getInstance().getReference();

        if(isGPSEnabled())
        {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            getSavedLocation();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        searchIcon.setOnClickListener(this::searchLocate);
    }

    private void searchLocate(View view) {
        String locationAddress = locSearch.getText().toString();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(locationAddress, 1);

            if(addressList.size() > 0)
            {
                Address address = addressList.get(0);

                gotoLocation(address.getLatitude(), address.getLongitude());

                map.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));

                Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
        }
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(providerEnabled)
        {
            return true;
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS is required for this apps to work. Please enable GPS Location.")
                    .setPositiveButton("Yes", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent,9001);
                    }))
                    .setCancelable(false).show();
        }

        return false;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                gotoLocation(location.getLatitude(), location.getLongitude());
                Context context = getApplicationContext();

                com.project.Address address = new com.project.Address();
                address.setLatitude(String.valueOf(location.getLatitude()));
                address.setLongitude(String.valueOf(location.getLongitude()));

                firebase.child("Location").child("address").setValue(address);
                Toast.makeText(context, String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng currentLocation = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
        map.moveCamera(cameraUpdate);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions().position(currentLocation));
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        defaultLocation = new LatLng(3.137730, 101.684260);
        map.addMarker(new MarkerOptions().position(defaultLocation).title("Kuala Lumpur"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, 18);
        map.moveCamera(cameraUpdate);
    }

    private void getSavedLocation()
    {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Location").child("address");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String latitude = snapshot.child("latitude").getValue().toString();
                    String longitude = snapshot.child("longitude").getValue().toString();

                    defaultLocation = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                    map.addMarker(new MarkerOptions().position(defaultLocation).title("Current Location"));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, 18);
                    map.moveCamera(cameraUpdate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9001) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (providerEnabled) {
                Toast.makeText(this, "GPS is enable", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS is not enable", Toast.LENGTH_SHORT).show();
            }
        }
    }
}