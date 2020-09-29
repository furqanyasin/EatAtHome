package com.example.eatathome.Server.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eatathome.R;
import com.example.eatathome.Server.Constant.ConstantRes;
import com.example.eatathome.Server.Models.UserRes;
import com.example.eatathome.Server.ViewHolder.UserViewHolderRes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManageAccountActivity extends AppCompatActivity {

    FloatingActionButton fabAddStaff;
    //Firebase
    FirebaseDatabase db;
    DatabaseReference users;
    FirebaseRecyclerAdapter<UserRes, UserViewHolderRes> adapter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        //Init View
        fabAddStaff =  findViewById(R.id.fab_add_staff);
        fabAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateAccountDialog();
            }
        });

        db = FirebaseDatabase.getInstance();
        users = db.getReference(ConstantRes.Staff_TABLE);

        recyclerView =  findViewById(R.id.recycler_account);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadAccount();
    }


    private void loadAccount() {
        FirebaseRecyclerOptions<UserRes> options = new FirebaseRecyclerOptions.Builder<UserRes>()
                .setQuery(users, UserRes.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<UserRes, UserViewHolderRes>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolderRes viewHolder, final int position, @NonNull final UserRes model) {
                viewHolder.staffName.setText(model.getPhone());
                viewHolder.staffPassword.setText(model.getName());
                viewHolder.staffRole.setText(ConstantRes.convertRole(model.getIsstaff()));

                //new event
                viewHolder.btnEditAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditAccountDialog(adapter.getRef(position).getKey(), model);
                    }
                });


                viewHolder.btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteAccountDialog(adapter.getRef(position).getKey());
                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolderRes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_manage_account_info, parent, false);
                return new UserViewHolderRes(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void showCreateAccountDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManageAccountActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("CREATE STAFF ACCOUNT");
        alertDialog.setMessage("Please fill in all information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_account = inflater.inflate(R.layout.create_account_layout, null);

        final TextInputEditText account_phone = layout_account.findViewById(R.id.et_create_account_phone);
        final TextInputEditText account_name = layout_account.findViewById(R.id.et_create_account_name);
        final TextInputEditText account_password = layout_account.findViewById(R.id.et_create_account_password);
        account_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        account_password.setTransformationMethod(new PasswordTransformationMethod());
        alertDialog.setView(layout_account);
        alertDialog.setIcon(R.drawable.ic_create_black_24dp);


        alertDialog.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                //create account

                if (TextUtils.isEmpty(account_phone.getText())) {
                    Toast.makeText(ManageAccountActivity.this, "Phone Number is Empty!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(account_name.getText())) {
                    Toast.makeText(ManageAccountActivity.this, "Username is Empty!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(account_password.getText())) {
                    Toast.makeText(ManageAccountActivity.this, "Password is Empty!", Toast.LENGTH_SHORT).show();
                } else if (account_phone.getText().length() < 11) {
                    Toast.makeText(ManageAccountActivity.this, "Phone Number cannot less than 11 digts!", Toast.LENGTH_SHORT).show();
                } else if (account_phone.getText().length() > 13) {
                    Toast.makeText(ManageAccountActivity.this, "Phone Number cannot exceed 13 digits!", Toast.LENGTH_SHORT).show();
                } else {
                    UserRes user = new UserRes();
                    user.setPhone(account_phone.getText().toString());
                    user.setName(account_name.getText().toString());
                    user.setPassword(account_password.getText().toString());
                    user.setIsstaff("true");
                    user.setIsadmin("false");

                    users.child(account_phone.getText().toString())
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ManageAccountActivity.this, "Staff Created Successfully!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ManageAccountActivity.this, "Failed to Create Account!", Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void showEditAccountDialog(final String key, final UserRes model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManageAccountActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("UPDATE ACCOUNT");
        alertDialog.setMessage("Please fill in all information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_account = inflater.inflate(R.layout.create_account_layout, null);

        final TextInputEditText account_phone = layout_account.findViewById(R.id.et_create_account_phone);
        final TextInputEditText account_name = layout_account.findViewById(R.id.et_create_account_name);
        final TextInputEditText account_password = layout_account.findViewById(R.id.et_create_account_password);
        account_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        account_password.setTransformationMethod(new PasswordTransformationMethod());

        //set data
        account_name.setText(model.getName());
        account_password.setText(model.getPassword());
        account_phone.setText(model.getPhone());
        account_phone.setEnabled(false);

        alertDialog.setView(layout_account);
        alertDialog.setIcon(R.drawable.ic_create_black_24dp);


        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                //create account

                if (TextUtils.isEmpty(account_phone.getText())) {
                    Toast.makeText(ManageAccountActivity.this, "Phone Number is Empty!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(account_name.getText())) {
                    Toast.makeText(ManageAccountActivity.this, "Username is Empty!", Toast.LENGTH_SHORT).show();
                } else if (account_phone.getText().length() < 11) {
                    Toast.makeText(ManageAccountActivity.this, "Phone Number cannot less than 11 digts!", Toast.LENGTH_SHORT).show();
                } else if (account_phone.getText().length() > 13) {
                    Toast.makeText(ManageAccountActivity.this, "Phone Number cannot exceed 13 digits!", Toast.LENGTH_SHORT).show();
                } else {
                    UserRes user = new UserRes();
                    user.setPhone(account_phone.getText().toString());
                    user.setName(account_name.getText().toString());
                    user.setPassword(account_password.getText().toString());
                    user.setIsstaff("true");
                    user.setIsadmin("false");

                    users.child(account_phone.getText().toString())
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ManageAccountActivity.this, "Staff Created Successfully!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ManageAccountActivity.this, "Failed to Create Account!", Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void showDeleteAccountDialog(final String key) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManageAccountActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Confirm Delete?");

        LayoutInflater inflater = this.getLayoutInflater();
        View confirm_delete_layout = inflater.inflate(R.layout.confirm_delete_layout, null);
        alertDialog.setView(confirm_delete_layout);
        alertDialog.setIcon(R.drawable.ic_delete);

        alertDialog.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                users.child(key).removeValue();
                Toast.makeText(ManageAccountActivity.this, "Account Delete Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
        loadAccount();
    }
}