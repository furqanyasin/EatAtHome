package com.example.eatathome.Client.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.eatathome.Constant.Constant;
import com.example.eatathome.Models.ClientUsers;
import com.example.eatathome.R;
import com.example.eatathome.databinding.ActivitySignInBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    public ActivitySignInBinding activitySignInBinding;
    String phoneNumber, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignInBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        activitySignInBinding.btnSignIn.setOnClickListener(this);
        activitySignInBinding.txtSignUp.setOnClickListener(this);
        activitySignInBinding.chbRemember.setOnClickListener(this);

        //init paper
        Paper.init(this);

        //check remember
        String user = Paper.book().read(Constant.USER_KEY);
        String password = Paper.book().read(Constant.PASSWORD_KEY);
        if (user != null && password != null) {
            if (!user.isEmpty() && !password.isEmpty())
                login(user, password);
        }

    }

    private void login(final String user, final String password) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_users = database.getReference("users");

        if (Constant.isConnectedToInternet(getBaseContext())) {

            final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
            mDialog.setMessage("Please waiting...");
            mDialog.show();

            table_users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //check if user not exit in database
                    if (dataSnapshot.child(user).exists()) {
                        //Get User Information
                        mDialog.dismiss();
                        ClientUsers clientUsers = dataSnapshot.child(user).getValue(ClientUsers.class);
                        clientUsers.setPhone(user);
                        if (clientUsers.getPassword().equals(password)) {
                            Toast.makeText(SignInActivity.this, "Sign In Successfully !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, RestaurantListActivity.class);
                            Constant.currentUser = clientUsers;
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(SignInActivity.this, "Wrong Password!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "User not exists ", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        if (view == activitySignInBinding.btnSignIn) {
            buttonSignIn();
        } else if (view == activitySignInBinding.txtSignUp) {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        }

    }

    public void buttonSignIn() {

        phoneNumber = activitySignInBinding.etPhoneNumber.getText().toString();
        password = activitySignInBinding.etPassword.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_users = database.getReference("users");

        if (Constant.isConnectedToInternet(getBaseContext())) {

            //save user & password
            if (activitySignInBinding.chbRemember.isChecked()) {
                Paper.book().write(Constant.USER_KEY, phoneNumber);
                Paper.book().write(Constant.PASSWORD_KEY, password);
            }


            final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
            mDialog.setMessage("Please waiting...");
            mDialog.show();

            table_users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //check if user not exit in database
                    if (dataSnapshot.child(activitySignInBinding.etPhoneNumber.getText().toString()).exists()) {
                        //Get User Information
                        mDialog.dismiss();
                        ClientUsers clientUsers = dataSnapshot.child(activitySignInBinding.etPhoneNumber.getText().toString()).getValue(ClientUsers.class);
                        clientUsers.setPhone(activitySignInBinding.etPhoneNumber.getText().toString());
                        if (clientUsers.getPassword().equals(activitySignInBinding.etPassword.getText().toString())) {
                            Toast.makeText(SignInActivity.this, "Sign In Successfully !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, RestaurantListActivity.class);
                            Constant.currentUser = clientUsers;
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(SignInActivity.this, "Wrong Password!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "User not exists ", Toast.LENGTH_SHORT).show();
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