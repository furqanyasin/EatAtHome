package com.example.eatathome.Server.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import com.example.eatathome.R;

import com.example.eatathome.databinding.ActivityRestaurantMainBinding;

public class RestaurantMainActivity extends AppCompatActivity implements View.OnClickListener {

    public ActivityRestaurantMainBinding activityRestaurantMainBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRestaurantMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        activityRestaurantMainBinding.txtSlogan.setTypeface(typeface);
        activityRestaurantMainBinding.txtLogo.setTypeface(typeface);

        // set on click listener
        activityRestaurantMainBinding.btnStaffSignIn.setOnClickListener(this);
        activityRestaurantMainBinding.btnAdminSignIn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == activityRestaurantMainBinding.btnStaffSignIn) {
            Intent intent = new Intent(this, StaffSignInActivity.class);
            startActivity(intent);

        } else if (view == activityRestaurantMainBinding.btnAdminSignIn) {
            Intent intent = new Intent(this, AdminSignInActivity.class);
            startActivity(intent);
        }

    }
}