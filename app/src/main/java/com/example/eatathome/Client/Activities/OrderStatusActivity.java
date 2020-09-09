package com.example.eatathome.Client.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatathome.R;
import com.example.eatathome.Constant.Constant;
import com.example.eatathome.Models.Request;
import com.example.eatathome.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatusActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    FirebaseRecyclerOptions<Request> firebaseRecyclerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //init firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Request");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Request>().setQuery(requests, Request.class).build();

        //Load menu
        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent()==null)
            loadOrder(Constant.currentUser.getPhone());
        else
            loadOrder(getIntent().getStringExtra("userPhone"));
    }

    private void loadOrder(String phone) {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request model) {
                holder.txtOrderID.setText(adapter.getRef(position).getKey());
                holder.txtOrderPhone.setText(model.getPhone());
                holder.txtOrderStatus.setText(Constant.convertCodeToStatus(model.getStatus()));
                holder.txtOrderAddress.setText(model.getAddress());
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new OrderViewHolder(view);
            }
        };

        //set adapter
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}