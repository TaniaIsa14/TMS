package com.itvillage.tms.tms_transportmanagementsystem.asystask;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import im.delight.android.location.SimpleLocation;

/**
 * Created by monirozzamanroni on 7/23/2020.
 */

public class LocationAsynTask extends AsyncTask<Void,Integer,SimpleLocation>{

    Context context;

    public LocationAsynTask(Context context) {
        this.context = context;
    }




    @Override
    protected SimpleLocation doInBackground(Void... voids) {
        return new SimpleLocation(this.context ); //or context instead of this

    }
}
