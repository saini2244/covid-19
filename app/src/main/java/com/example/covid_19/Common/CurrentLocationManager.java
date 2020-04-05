package com.example.covid_19;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class CurrentLocationManager implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Context mContext;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    List<android.location.Address> geocodeMatches = null;
    private SharedPreferences sharedPref;
    private boolean isconnected = false;

    public boolean isConnected() {
        return isconnected;
    }

    CurrentLocationManager(Context mContext) {
        this.mContext = mContext;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        sharedPref = mContext.getSharedPreferences(
                mContext.getApplicationContext().getString(R.string.preference_file_key), mContext.MODE_PRIVATE);
        getLastLocation();
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    SaveLocation(location);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this.mContext, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                this.mContext.startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.mContext);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    public void SaveLocation(Location location) {
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        SharedPreferences.Editor myEdit = sharedPref.edit();

        System.out.println(location.describeContents() + "");
        // Storing the key and its value
        // as the data fetched from edittext
        myEdit.putString("lat", location.getLatitude() + "");
        myEdit.putString("long", location.getLongitude() + "");

        try {
            geocodeMatches =
                    new Geocoder(this.mContext).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!geocodeMatches.isEmpty()) {
                Address address = geocodeMatches.get(0);
                myEdit.putString("Address", address.getAddressLine(0) + "");
                myEdit.putString("Address1", address.getAddressLine(1) + "");
                myEdit.putString("state", address.getAdminArea() + "");
                myEdit.putString("zipcode", address.getPostalCode() + "");
                myEdit.putString("country", address.getCountryName() + "");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();
        this.isconnected = true;
        Toast.makeText(mContext, "" + sharedPref.getString("country", ""), Toast.LENGTH_SHORT).show();
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            SaveLocation(mLastLocation);
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this.mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                (Activity) this.mContext,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                this.isconnected = true;
            }
        }
    }
}
