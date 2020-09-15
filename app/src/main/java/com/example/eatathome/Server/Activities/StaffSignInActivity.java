package com.example.eatathome.Server.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eatathome.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class StaffSignInActivity extends AppCompatActivity {

    MaterialButton btnSignIn;
    TextInputEditText Phone, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_sign_in);
    }
}