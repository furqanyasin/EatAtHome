package com.example.eatathome.Client.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.eatathome.Client.Constant.Constant;
import com.example.eatathome.Client.Model.User;
import com.example.eatathome.R;
import com.example.eatathome.databinding.ActivitySignUpBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding activitySignUpBinding;
    String phoneNumber, name, password, secureCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        activitySignUpBinding.btnSignUp.setOnClickListener(this);
        activitySignUpBinding.txtSignIn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == activitySignUpBinding.btnSignUp) {

            buttonSignUp();

        } else if (view == activitySignUpBinding.txtSignIn) {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        }

    }

    public void buttonSignUp() {

        if (!validatePhoneNumber() | !validateName() | !validatePassword() | !validateSecureCode()) {
            Toast.makeText(this, "Please enter correct info ", Toast.LENGTH_SHORT).show();

        } else {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_users = database.getReference("User");

            if (Constant.isConnectedToInternet(getBaseContext())) {
                final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //check if user not exit in database
                        if (dataSnapshot.child(phoneNumber).exists()) {
                            //Get User Information
                            mDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Phone Number already exists", Toast.LENGTH_SHORT).show();

                        } else {
                            mDialog.dismiss();
                            User clientUsers = new User(name, password, phoneNumber, secureCode, null, null);
                            table_users.child(phoneNumber).setValue(clientUsers);
                            Toast.makeText(SignUpActivity.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                return;

            }
        }


    }

    private boolean validatePhoneNumber() {
        phoneNumber = activitySignUpBinding.etPhoneNumber.getText().toString();
        if (phoneNumber.isEmpty()) {
            activitySignUpBinding.etPhoneNumber.setError("Phone Number is required. Can't be empty.");
            return false;
        } else if (phoneNumber.length() < 11) {
            activitySignUpBinding.etPhoneNumber.setError("Phone Number cannot less than 11 digits!");
            return false;
        } else if (phoneNumber.length() > 13) {
            activitySignUpBinding.etPhoneNumber.setError("Phone Number cannot exceed 13 digits!");
            return false;
        } else {
            activitySignUpBinding.etPhoneNumber.setError(null);
            return true;
        }

    }

    private boolean validateSecureCode() {

        secureCode = activitySignUpBinding.etSecureCode.getText().toString();

        if (secureCode.isEmpty()) {
            activitySignUpBinding.etSecureCode.setError("SecureCode is required. Can't be empty.");
            return false;
        } else if (secureCode.length() < 8) {
            activitySignUpBinding.etSecureCode.setError("SecureCode length short. Minimum 8 characters required.");
            return true;
        } else {
            activitySignUpBinding.etSecureCode.setError(null);
            return true;
        }
    }

    private boolean validateName() {

        name = activitySignUpBinding.etName.getText().toString();

        if (name.isEmpty()) {
            activitySignUpBinding.etName.setError("UserName is required. Can't be empty.");
            return false;
        } else {
            activitySignUpBinding.etName.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        password = activitySignUpBinding.etPassword.getText().toString();

        if (password.isEmpty()) {
            activitySignUpBinding.etPassword.setError("Password is required. Can't be empty.");
            return false;
        } else if (password.length() < 8) {
            activitySignUpBinding.etPassword.setError("Password length short. Minimum 8 characters required.");
            return true;
        } else {
            activitySignUpBinding.etPassword.setError(null);
            return true;
        }
    }


}