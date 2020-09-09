package com.example.eatathome.Server.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eatathome.Constant.Constant;
import com.example.eatathome.Interface.ItemClickListener;
import com.example.eatathome.Models.Request;
import com.example.eatathome.Models.Restaurant;
import com.example.eatathome.R;
import com.example.eatathome.Server.Activities.ViewHolder.OrderViewHolderRes;
import com.example.eatathome.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusActivityRes extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    MaterialSpinner spinner;

    FirebaseRecyclerAdapter<Request, OrderViewHolderRes> adapter;
    FirebaseRecyclerOptions<Request> firebaseRecyclerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_res);

        //init firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Request");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Request>().setQuery(requests, Request.class).build();

        //Load menu
        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        loadOrders();
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolderRes>(firebaseRecyclerOptions) {

            @NonNull
            @Override
            public OrderViewHolderRes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new OrderViewHolderRes(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolderRes holder, int position, @NonNull final Request model) {
                holder.txtOrderID.setText(adapter.getRef(position).getKey());
                holder.txtOrderPhone.setText(model.getPhone());
                holder.txtOrderStatus.setText(Constant.convertCodeToStatus(model.getStatus()));
                holder.txtOrderAddress.setText(model.getAddress());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent trackingOrder = new Intent(OrderStatusActivityRes.this, TrackingOrderActivityRes.class);
                        Constant.currentRequest = model;
                        startActivity(trackingOrder);
                    }
                });

            }


        };
        //set adapter
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Constant.UPDATE))
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        else if (item.getTitle().equals(Constant.DELETE))
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        return super.onContextItemSelected(item);
    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
    }

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatusActivityRes.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please Chose Status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        spinner = (MaterialSpinner) view.findViewById(R.id.spinner);
        spinner.setItems("Placed", "On my way", "Shipped");

        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));

                requests.child(localKey).setValue(item);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        alertDialog.show();
    }

}