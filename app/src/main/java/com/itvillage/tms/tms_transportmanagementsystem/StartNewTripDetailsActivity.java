package com.itvillage.tms.tms_transportmanagementsystem;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itvillage.tms.tms_transportmanagementsystem.asystask.LocationAsynTask;
import com.itvillage.tms.tms_transportmanagementsystem.dto.response.UserDetailsResponse;
import com.itvillage.tms.tms_transportmanagementsystem.util.Utility;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import im.delight.android.location.SimpleLocation;

public class StartNewTripDetailsActivity extends AppCompatActivity {

    String fromText, toTExt, startTimeTExt, transportNameTExt, transportNumberTExt, userId;
    private final String TAG = "Start New Trip";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    TimePickerDialog picker;
    Handler handler;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_trip_details);
        Button next = findViewById(R.id.next);
        final TextInputEditText fromEditTExt = findViewById(R.id.from_editText);
        final TextInputEditText toEditTExt = findViewById(R.id.to_editText);
        final TextInputEditText startTimeEditTExt = findViewById(R.id.sTime_editText);
        final TextInputEditText transportNameEditTExt = findViewById(R.id.tName_editText);
        final TextInputEditText transportNumberEditTExt = findViewById(R.id.tNumber_editText);
        userId = UserDetailsResponse.userId;


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("trips").child(userId);

        startTimeEditTExt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(StartNewTripDetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                startTimeEditTExt.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fromText = fromEditTExt.getText().toString();
                toTExt = toEditTExt.getText().toString();
                startTimeTExt = startTimeEditTExt.getText().toString();
                transportNameTExt = transportNameEditTExt.getText().toString();
                transportNumberTExt = transportNumberEditTExt.getText().toString();

                saveTripInfoInDB();


            }
        });
    }

    private void saveTripInfoInDB() {

        if (isValidate()) {

            myRef.child("userId").setValue(userId);
            myRef.child("from").setValue(fromText);
            myRef.child("from").setValue("Waiting");
            myRef.child("to").setValue(toTExt);
            myRef.child("startTime").setValue(startTimeTExt);
            myRef.child("endTime").setValue("");
            myRef.child("transportName").setValue(transportNameTExt);
            myRef.child("transportNo").setValue(transportNumberTExt);
            myRef.child("status").setValue("true");
            if (isLocationEnabled()) {
                getLocationLatLong();
                Utility.intent(getApplicationContext(), ConfirmationForOpenMapActivity.class);
            }

        }
    }

    private void getLocationLatLong() {

        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                LocationAsynTask locationAsynTask = new LocationAsynTask(getApplicationContext());
                locationAsynTask.execute();
                try {
                    SimpleLocation simpleLocation = locationAsynTask.get();
                    if (simpleLocation == null) {
                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Location getting null");
                    } else {
                        myRef.child("lat").setValue(simpleLocation.getLatitude());
                        myRef.child("long").setValue(simpleLocation.getLongitude());
                        Log.d("Latitude", String.valueOf(simpleLocation.getLatitude()));
                        Log.d("Longitude", String.valueOf(simpleLocation.getLongitude()));
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

    }

    private boolean isValidate() {
        Boolean isValidate = false;
        if (fromText.equals("") || toTExt.equals("") || startTimeTExt.equals("") || transportNameTExt.equals("") ||
                transportNumberTExt.equals("")) {
            Toast.makeText(this, "Empty field found.", Toast.LENGTH_SHORT).show();
        } else {
            isValidate = true;
        }
        return isValidate;
    }

    private boolean isLocationEnabled() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_name)
                        .setMessage("Need to open your gps.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(StartNewTripDetailsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //  locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
