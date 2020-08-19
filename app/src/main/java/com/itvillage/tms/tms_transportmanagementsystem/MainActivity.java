package com.itvillage.tms.tms_transportmanagementsystem;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itvillage.tms.tms_transportmanagementsystem.dto.response.UserDetailsResponse;
import com.itvillage.tms.tms_transportmanagementsystem.util.Utility;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView signUp;
    private Button signIn;
    private TextInputEditText email, password;
    private String userEmail, userPassword;
    private final String TAG = "Main Activity";
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUp = findViewById(R.id.sign_up);
        signIn = findViewById(R.id.sign_in);
        final TextInputEditText emailEditText = findViewById(R.id.email_login);
        final TextInputEditText passwordEditText = findViewById(R.id.password_login);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.intent(getApplicationContext(), RegistrationActivity.class);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = emailEditText.getText().toString();
                userPassword = passwordEditText.getText().toString();
                if(Utility.isNetworkAvailable(getApplicationContext())) {
                    progress.show();
                    login();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No internet connected", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void login() {

        final boolean[] isEmailPresent = {false};
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Employee");
        if (isValidate()) {
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        String email = child.child("email").getValue().toString();
                        String password = child.child("password").getValue().toString();
                        if (email.equals(userEmail) && password.equals(userPassword)) {
                            isEmailPresent[0] = true;
                            String userId = child.child("userId").getValue().toString();
                            String userType = child.child("userType").getValue().toString();
                            String firstName = child.child("firstName").getValue().toString();
                            String lastName = child.child("lastName").getValue().toString();
                            String phoneNumber = child.child("phoneNumber").getValue().toString();

                            UserDetailsResponse.userId = userId;
                            UserDetailsResponse.firstName=firstName;
                            UserDetailsResponse.lastName=lastName;
                            UserDetailsResponse.phoneNo=phoneNumber;
                            UserDetailsResponse.role=userType;
                            UserDetailsResponse.email=email;

                            if (userType.toLowerCase().equals("employee")) {
                                Utility.intent(getApplicationContext(), EmployeesHomeActivity.class);
                                progress.dismiss();

                            } else if (userType.toLowerCase().equals("driver")) {
                                Utility.intent(getApplicationContext(), DriverHomeActivity.class);
                                progress.dismiss();

                            } else {
                                Toast.makeText(getApplicationContext(), "User role not found.", Toast.LENGTH_SHORT).show();
                            }

                            Log.d(TAG, userId);
                        } else {

                        }
                    }
                    if (!isEmailPresent[0]) {
                        Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Log.d(TAG, "Done");

        }

    }

    private boolean isValidate() {
        Boolean isValidate = false;

        if (userEmail.equals("")
                || userPassword.equals("")) {
            Log.d(TAG, "Empty Field Found");
        } else {
            if (userEmail.length() > 10) {
                String emailDomainName = userEmail.substring(userEmail.length() - 10, userEmail.length());
                Log.d(TAG, "Found Domain " + emailDomainName);
                if (emailDomainName.equals("diu.edu.bd")) {
                    isValidate = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
            }

        }

        return isValidate;
    }
}
