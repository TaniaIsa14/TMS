package com.itvillage.tms.tms_transportmanagementsystem;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itvillage.tms.tms_transportmanagementsystem.dto.response.UserDetailsResponse;
import com.itvillage.tms.tms_transportmanagementsystem.util.Utility;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class DriverHomeActivity extends AppCompatActivity  {
    CardView newTripBut;
    TextView namTextView,designationnTextView,emailTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

         newTripBut = findViewById(R.id.newTripBut);
         namTextView= findViewById(R.id.nameTextView);
         designationnTextView= findViewById(R.id.designationnTextView);
         emailTextView= findViewById(R.id.emailTextView);
        setProfileDetails();




        newTripBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.intent(getApplicationContext(),StartNewTripDetailsActivity.class);
            }
        });
    }

    private void setProfileDetails() {
        namTextView.setText(UserDetailsResponse.firstName+" " +UserDetailsResponse.lastName);
        //TODO : add Designation in DesignationTextView
        emailTextView.setText(UserDetailsResponse.email);

    }
}
