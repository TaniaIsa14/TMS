package com.itvillage.tms.tms_transportmanagementsystem;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itvillage.tms.tms_transportmanagementsystem.util.Utility;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private String lastName, phoneNumber, firstName, email, password, userType;
    private final String TAG = "RegistrationActivity";
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Spinner userTypeSpinner = (Spinner) findViewById(R.id.spinner);
        Button signUp = findViewById(R.id.sign_up);

        final TextInputEditText fName = findViewById(R.id.fName);
        final TextInputEditText lName = findViewById(R.id.lName);
        final TextInputEditText phoneNo = findViewById(R.id.phoneNo);
        final TextInputEditText emailEditText = findViewById(R.id.email);
        final TextInputEditText passwordEditText = findViewById(R.id.password);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        List<String> categories = new ArrayList<String>();
        categories.add("Select User Type");
        categories.add("Employee");
        categories.add("Driver");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userTypeSpinner.setAdapter(dataAdapter);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = fName.getText().toString();
                lastName = lName.getText().toString();
                phoneNumber = phoneNo.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if(Utility.isNetworkAvailable(getApplicationContext())) {
                    progress.show();
                    registration(userTypeSpinner.getSelectedItem().toString());

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No internet connected", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private String registration(String userType) {
        this.userType = userType;
        String userId=Utility.getUUID();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Employee").push();

        if (isValidate()) {
            myRef.child("userId").setValue(userId);
            myRef.child("userType").setValue(this.userType);
            myRef.child("firstName").setValue(firstName);
            myRef.child("lastName").setValue(lastName);
            myRef.child("phoneNumber").setValue(phoneNumber);
            myRef.child("email").setValue(email);
            myRef.child("password").setValue(password);
            Log.d(TAG, "Add In Firebase DB");
            Utility.intent(getApplicationContext(),MainActivity.class);
            progress.show();
        }
        return userId;
    }

    private boolean isValidate() {
        Boolean isValidate = false;
        if (this.userType.equals("Select User Type")) {
            Log.d(TAG, "Select Type");
            Toast.makeText(getApplicationContext(), "Select user type", Toast.LENGTH_SHORT).show();

        } else {
            if (firstName.equals("")
                    || lastName.equals("")
                    || phoneNumber.equals("")
                    || email.equals("")
                    || password.equals("")) {
                Log.d(TAG, "Empty Field Found");
            } else {
                if(email.length() > 10) {
                    String emailDomainName = email.substring(email.length() - 10, email.length());
                    Log.d(TAG, "Found Domain " + emailDomainName);
                    if (emailDomainName.equals("diu.edu.bd")) {
                        isValidate = true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                }

            }

        }

        return isValidate;
    }
}