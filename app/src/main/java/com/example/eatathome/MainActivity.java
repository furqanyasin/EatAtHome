package com.example.eatathome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.eatathome.Client.Activities.RestaurantListActivity;
import com.example.eatathome.Client.Activities.SignInActivity;
import com.example.eatathome.Client.Activities.SignUpActivity;
import com.example.eatathome.Constant.Constant;
import com.example.eatathome.Models.ClientUsers;
import com.example.eatathome.Server.Activities.SignInActivityRes;
import com.example.eatathome.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        activityMainBinding.txtSlogan.setTypeface(typeface);
        activityMainBinding.txtLogo.setTypeface(typeface);

        // set on click listener
        activityMainBinding.btnSignIn.setOnClickListener(this);
        activityMainBinding.btnSignInRestaurant.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if (view == activityMainBinding.btnSignIn) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);

        } else if (view == activityMainBinding.btnSignInRestaurant) {
            Intent intent = new Intent(this, SignInActivityRes.class);
            startActivity(intent);
        }

    }



}