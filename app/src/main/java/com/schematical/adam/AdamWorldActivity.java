package com.schematical.adam;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import com.schematical.adam.comm.socket.AdamSocketClient;
import com.schematical.adam.location.AdamLocation;
import com.schematical.adam.signal.AdamScanResultBase;
import com.schematical.adam.signal.AdamSignalDriver;
import com.schematical.adam.signal.gps.AdamGPSDriver;
import com.schematical.adam.storage.AdamStorageDriver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

public class AdamWorldActivity extends FragmentActivity {
    private static AdamWorldActivity instance;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    protected AdamSocketClient client;
    private AdamLocation lastLocation;
    private AdamSignalDriver signalDriver;
    private AdamScanResultBase target;


    public static AdamWorldActivity getInstance(){
        return instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
       setupUI();


        AdamGPSDriver gpsDriver = new AdamGPSDriver();
        signalDriver = new AdamSignalDriver();
        AdamSignalDriver.Connect();
        client = new AdamSocketClient();
        try {
            client.Connect("http://" + getString(R.string.schematical_socket_host));
        } catch (URISyntaxException e) {
            Log.d("Adam","FAILED:" + e.getMessage());
            e.printStackTrace();
        }

    }

    private void setupUI() {
        setContentView(R.layout.activity_adam_maps);
        setUpMapIfNeeded();
        Button menu = (Button) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                setContentView(R.layout.signal_list);
                final ListView listView = (ListView) findViewById(R.id.signal_list_view);
                Activity inst = getInstance();
                final AdamSignalListAdaptor adaptor =  new AdamSignalListAdaptor(inst);
                listView.setAdapter(adaptor);
                final SwipeRefreshLayout parentLayout = (SwipeRefreshLayout) findViewById(R.id.signal_list);
                parentLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        adaptor.refresh();
                        parentLayout.setRefreshing(false);

                    }
                });
            }
        });



        Button btnPing = (Button) findViewById(R.id.ping);
        btnPing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ping();
            }
        });
    }
    public void setTarget(AdamScanResultBase result){
        target = result;

    }

    protected  void logCacheFile(){
        //Log the cache file
        try {
            String[] data = AdamStorageDriver.readAsArray();
            for(int i = 0; i < data.length; i++) {
                Log.d("Adam", "Adam Storage Line:" + i + "\n" + data[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
    public void addPin(LatLng latLng){

    }

    public void onLocationChanged(AdamLocation aLocation) {

        lastLocation = aLocation;

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(aLocation.getLatitude(), aLocation.getLongitude()))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.clear();
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(aLocation.getLatitude(), aLocation.getLongitude()))
                .radius(aLocation.getAccuracy())
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));
       // ping();

    }
    public void ping(){
        ArrayList<Map> signals = AdamSignalDriver.GetResultsArray();
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("listener", lastLocation.toJSON());
            jObj.put("pings", new JSONArray(signals));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.Send("ping", jObj);

        Log.d("Adam", "Sending Data");
        try {
            AdamStorageDriver.write(jObj.toString());
        } catch (IOException e) {
            e.printStackTrace();

        }
        AdamSignalDriver.clearOldResults("Wifi");
    }
}
