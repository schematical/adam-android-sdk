package com.schematical.adam.signal;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user1a on 10/16/13.
 */
public class AdamScanResultBase {


    protected String alias;
    protected String mac;
    protected int rssi;
    protected String type;
    protected String extra;
    protected int frequency;
    protected long timestamp;
    public AdamScanResultBase(){
        timestamp = System.currentTimeMillis();
    }
    public String getAlias() {
        return alias;
    }

    public String getMac() {
        return mac;
    }

    public int getRssi() {
        return rssi;
    }

    public String getType() {
        return type;
    }

    public String getExtra() {
        return extra;
    }

    public int getFrequency() {
        return frequency;
    }
    public JSONObject toJSONObject() {
        JSONObject jObj = new JSONObject(
                this.toMap()
        );
        return jObj;

    }
    public Map<String, Object> toMap(){
        Map rMap = new HashMap<String, Object>();

        rMap.put("mac", this.mac);
        rMap.put("rssi", this.rssi);
        rMap.put("frequency", this.frequency);
        rMap.put("timestamp", this.timestamp);
        rMap.put("type", this.type);
        rMap.put("alias", this.alias);
        rMap.put("extra", this.extra);
        return rMap;
    }
}
