package com.itvillage.tms.tms_transportmanagementsystem;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.itvillage.tms.tms_transportmanagementsystem.asystask.LocationAsynTask;
import com.itvillage.tms.tms_transportmanagementsystem.dto.response.UserDetailsResponse;

import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import im.delight.android.location.SimpleLocation;

import static com.itvillage.tms.tms_transportmanagementsystem.StartNewTripDetailsActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class EmployeesHomeActivity extends AppCompatActivity {
    CardView activeTransBut,emergencyBut;
    TextView namTextView,designationnTextView,emailTextView;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_home);

        activeTransBut = findViewById(R.id.activeTripBut);
        emergencyBut = findViewById(R.id.emergencyBut);

        namTextView= findViewById(R.id.nameTextView);
        designationnTextView= findViewById(R.id.designationnTextView);
        emailTextView= findViewById(R.id.emailTextView);

        setProfileDetails();

        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        activeTransBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("userId", "all");
                startActivity(intent);
            }
        });
        emergencyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocationEnabled()) {

                    LocationAsynTask locationAsynTask = new LocationAsynTask(getApplicationContext());
                    locationAsynTask.execute();
                    try {
                        SimpleLocation simpleLocation = locationAsynTask.get();
                        if (simpleLocation == null) {
                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent2 = new Intent();
                            intent2.setAction(Intent.ACTION_SEND);
                            intent2.setType("text/html");
                            intent2.putExtra(Intent.EXTRA_EMAIL, new String[]{"eproni29@gmail.com"});
                            intent2.putExtra(Intent.EXTRA_SUBJECT, "Need Help");
                            intent2.putExtra(Intent.EXTRA_TEXT, "https://maps.google.com/?ll="+simpleLocation.getLatitude()+","+simpleLocation.getLongitude());
                            startActivity(intent2);

                            Log.d("Latitude", String.valueOf(simpleLocation.getLatitude()));
                            Log.d("Longitude", String.valueOf(simpleLocation.getLongitude()));
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            }
        });
    }
    private void setProfileDetails() {
        namTextView.setText(UserDetailsResponse.firstName+" " +UserDetailsResponse.lastName);
        //TODO : add Designation in DesignationTextView
        emailTextView.setText(UserDetailsResponse.email);

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
                                ActivityCompat.requestPermissions(EmployeesHomeActivity.this,
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
