package com.schematical.adam.signal;

import android.util.Log;

import com.schematical.adam.signal.bluetooth.AdamBluetoothDriver;
import com.schematical.adam.signal.gps.AdamGPSDriver;
import com.schematical.adam.signal.wifi.AdamWifiDriver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by user1a on 10/16/13.
 */
public class AdamSignalDriver {
    protected static Hashtable<String, AdamScanResultBase> aScanResults;
    protected static AdamBluetoothDriver adamBluetoothDriver;
    protected static AdamWifiDriver adamWifiDriver;
    protected static AdamGPSDriver adamGPSDriver;
    public AdamSignalDriver(){
        aScanResults = new Hashtable<String, AdamScanResultBase>();
        adamGPSDriver = new AdamGPSDriver();
        adamWifiDriver = new AdamWifiDriver();
        adamBluetoothDriver = new AdamBluetoothDriver();
    }
    public static void clearOldResults(String type){
        Enumeration<String> keys = aScanResults.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            //Log.d("Adam",key + ":" + aScanResults.get(key).getType() + "==" + type);
            if(aScanResults.get(key).getType() == type){
                aScanResults.remove(key);
            }
        }
        Log.d("Adam", "clearOldResults Finished: " + aScanResults.size());
    }
    public static void AddScanResult(AdamScanResultBase scanResult){

        String key = scanResult.getType() + ":" + scanResult.getMac();
        //Log.d("Adam", "Got Scan result: " + key);
        aScanResults.put(key, scanResult);
    }
    public static void Connect(){
        adamWifiDriver.Connect();
        adamBluetoothDriver.Connect();
    }
    public static void Disconnect(){
        adamWifiDriver.Disconnect();
        adamBluetoothDriver.Disconnect();
        adamGPSDriver.Disconnect();
    }
    public static ArrayList<JSONObject> GetJSON(){
        ArrayList<JSONObject> aReturn = new ArrayList<JSONObject>();
        Enumeration<String> keys = aScanResults.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamScanResultBase sr = aScanResults.get(key);
            aReturn.add(sr.toJSONObject());
        }
        return aReturn;

    }
    public static ArrayList<Map> GetResultsArray(){
        ArrayList<Map> aReturn = new ArrayList<Map>();
        Enumeration<String> keys = aScanResults.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamScanResultBase sr = aScanResults.get(key);
            aReturn.add(sr.toMap());
        }
        return aReturn;

    }
    public static ArrayList<AdamScanResultBase> GetResults(){
        ArrayList<AdamScanResultBase> aReturn = new ArrayList<AdamScanResultBase>();
        Enumeration<String> keys = aScanResults.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamScanResultBase sr = aScanResults.get(key);
            aReturn.add(sr);
        }
        return aReturn;

    }
}
