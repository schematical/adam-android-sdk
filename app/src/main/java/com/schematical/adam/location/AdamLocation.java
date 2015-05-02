package com.schematical.adam.location;

import android.location.Location;
import android.provider.Settings;

import com.schematical.adam.AdamWorldActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user1a on 4/18/15.
 */
public class AdamLocation extends Location {


    protected long measureDate;

    public AdamLocation(Location l) {
        super(l);
        measureDate = System.currentTimeMillis();
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject jObj = new JSONObject();
        String  android_id = Settings.Secure.getString(AdamWorldActivity.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);


        jObj.put("alias", "android_id-" + android_id);
        jObj.put("lat", this.getLatitude());
        jObj.put("lng", this.getLongitude());
        jObj.put("altitude", this.getAltitude());
        jObj.put("accuracy", this.getAccuracy());
        jObj.put("measure_date", this.measureDate);

        return jObj;
    }
}
