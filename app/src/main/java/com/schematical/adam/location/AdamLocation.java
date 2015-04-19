package com.schematical.adam.location;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user1a on 4/18/15.
 */
public class AdamLocation extends Location {


    public AdamLocation(Location l) {
        super(l);
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject jObj = new JSONObject();
        jObj.put("lat", this.getLatitude());
        jObj.put("lng", this.getLatitude());
        jObj.put("altitude", this.getAltitude());
        jObj.put("accuracy", this.getAccuracy());

        return jObj;
    }
}
