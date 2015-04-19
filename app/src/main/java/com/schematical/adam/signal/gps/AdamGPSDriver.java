package com.schematical.adam.signal.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.schematical.adam.AdamWorldActivity;
import com.schematical.adam.location.AdamLocation;
import com.schematical.adam.signal.iAdamSignalDriver;


/**
 * Created by user1a on 10/16/13.
 */
public class AdamGPSDriver implements iAdamSignalDriver, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener  {


    private GoogleApiClient mGoogleApiClient;

    public AdamGPSDriver(){

        AdamWorldActivity am = AdamWorldActivity.getInstance();
        buildGoogleApiClient(am);
        mGoogleApiClient.connect();
        // Acquire a reference to the system Location Manager
        //LocationManager locationManager = (LocationManager) am.getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        //am.SetStatus("Waiting for GPS");

        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }
    protected synchronized void buildGoogleApiClient(Activity context) {

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.d("Adam", "GOOGLE API CLIENT CREATED!");
    }

    public void Connect(){}
    public void Disconnect(){}
    public void onLocationChanged(Location location) {
        //AdamWorldActivity.getInstance().addPin(new LatLng(location.getLatitude(), location.getLongitude()));
        AdamLocation aLocation = new AdamLocation(location);
        Log.d("Adam", "LOCATION CHANGED!");
        AdamWorldActivity.getInstance().onLocationChanged(aLocation);

    }
/*
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {
        //AdamWorldActivity.SetStatus("GPS Enabled");
    }

    public void onProviderDisabled(String provider) {
        //AdamWorldActivity.SetStatus("GPS Disabled");
    }*/


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Adam", "CONNECTED!");
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Adam", "DISCONNECTED!");
    }

   /* @Override
    public void onDisconnected() {
        Log.d("Adam", "DISCONNECTED!");
    }*/

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Adam", "Connection Failed :(");
    }

}
