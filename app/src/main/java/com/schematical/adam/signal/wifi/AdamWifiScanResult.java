package com.schematical.adam.signal.wifi;

import android.net.wifi.ScanResult;

import com.schematical.adam.signal.AdamScanResultBase;

/**
 * Created by user1a on 10/3/13.
 */
public class AdamWifiScanResult extends AdamScanResultBase {
    public ScanResult device;
    protected String TYPE_CONST = "wifi";

    AdamWifiScanResult(ScanResult nDevice){
        type = TYPE_CONST;
        device = nDevice;
        alias = device.SSID;
        mac = device.BSSID;
        extra = device.capabilities;
        frequency = device.frequency;
        rssi = device.level;

    }

    //TODO: Add to JSON method


}
