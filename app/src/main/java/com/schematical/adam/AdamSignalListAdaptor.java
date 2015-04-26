package com.schematical.adam;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.schematical.adam.signal.AdamScanResultBase;
import com.schematical.adam.signal.AdamSignalDriver;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by user1a on 4/26/15.
 */
public class AdamSignalListAdaptor extends BaseAdapter {
    private Context mContext;
    private  ArrayList<AdamScanResultBase> signals;
    public AdamSignalListAdaptor(Context c) {
        mContext = c;
        refresh();
    }
    public void refresh(){
        signals = AdamSignalDriver.GetResults();
        notifyDataSetChanged();
    }

    public int getCount() {
        Log.d("Adam", "Signal Count: " + signals.size());
        return signals.size();
    }

    public AdamScanResultBase getItem(int position) {
        return signals.get(position);
    }

    public long getItemId(int position) {
        return signals.get(position).getRssi();
    }

    // create a new ImageView for each item referenced by the Adapter
    /*public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[0]);
        return imageView;
    }
*/
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("Adam", "getView" + position);
        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            //textView.setLayoutParams(new ListView.LayoutParams(85, 85));
           //textView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            textView.setPadding(20,20,20,20);
        } else {
            textView = (TextView) convertView;
        }
        final AdamScanResultBase result = getItem(position);
        textView.setText(result.getAlias() + "  (" + result.getRssi() + ")");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AdamWorldActivity) mContext).setTarget(result);
            }
        });
        return textView;
    }
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_stat
    };
}
