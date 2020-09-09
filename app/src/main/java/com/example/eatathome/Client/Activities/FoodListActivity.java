package com.example.eatathome.Client.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatathome.Interface.ItemClickListener;
import com.example.eatathome.R;
import com.example.eatathome.Constant.Constant;
import com.example.eatathome.Models.Food;
import com.example.eatathome.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodListActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference foodList;
    RecyclerView recyclerViewFood;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    FirebaseRecyclerOptions<Food> firebaseRecyclerOptions;
    String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Food List");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(FoodListActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });

        //init firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Restaurants").child(Constant.restaurantSelected).child("detail").child("Foods");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodList, Food.class).build();

        //Load menu
        recyclerViewFood = findViewById(R.id.recyclerview_menu1);
        recyclerViewFood.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerViewFood.setLayoutManager(layoutManager);

        // get intent here
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra(Constant.CATEGORY_ID);
        if (!categoryId.isEmpty() && categoryId != null) {

            if (Constant.isConnectedToInternet(this))
                loadFood();
            else {
                Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void loadFood() {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.txtFoodName.setText(model.getName());
                Picasso.get().load(model.getImage())
                        .into(holder.foodImage);
                final Food clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(HomeActivity.this, "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                        //Get CategoryId and send to new activity
                        Intent foodDetails = new Intent(FoodListActivity.this, FoodDetailsActivity.class);
                        //Because foodId is a key, so we just key of this item
                        foodDetails.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodDetails);
                    }
                });

            }
        };
        //set adapter
        recyclerViewFood.setAdapter(adapter);
        adapter.startListening();
    }

}