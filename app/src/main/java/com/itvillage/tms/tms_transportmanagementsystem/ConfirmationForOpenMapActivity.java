package com.itvillage.tms.tms_transportmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itvillage.tms.tms_transportmanagementsystem.dto.response.UserDetailsResponse;
import com.itvillage.tms.tms_transportmanagementsystem.util.Utility;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationForOpenMapActivity extends AppCompatActivity {

    Button startTrip;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_for_open_map);
        startTrip= findViewById(R.id.start_trip);
        userId = UserDetailsResponse.userId;
        startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
               DatabaseReference myRef = database.getReference("trips").child(userId);
                myRef.child("from").setValue("On the way");
                Intent intent= new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("userId", UserDetailsResponse.userId);
                startActivity(intent);

            }
        });
    }
}
