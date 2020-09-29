package com.example.eatathome.Client.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatathome.Client.Database.Database;
import com.example.eatathome.Interface.ItemClickListener;
import com.example.eatathome.Client.Model.Favorites;
import com.example.eatathome.Client.Model.Food;
import com.example.eatathome.Client.Model.Order;
import com.example.eatathome.Client.ViewHolder.FoodViewHolder;
import com.example.eatathome.R;
import com.example.eatathome.Client.Constant.Constant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FoodListActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference foodList;
    RecyclerView recyclerViewFood;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    FirebaseRecyclerOptions<Food> firebaseRecyclerOptions;
    String categoryId = "";

    Database localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(" Food List");
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

        //local db
        localDB = new Database(this);


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
                loadFood(categoryId);
            else {
                Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void loadFood(String categoryId) {

        //create query by category ID
        Query searchByName = foodList.orderByChild("menuId").equalTo(categoryId);

        //create option with query
        FirebaseRecyclerOptions<Food> foodOptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchByName, Food.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Food, com.example.eatathome.Client.ViewHolder.FoodViewHolder>(foodOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final com.example.eatathome.Client.ViewHolder.FoodViewHolder viewHolder, final int position, @NonNull final Food model) {

                viewHolder.food_name.setText(model.getName());
                viewHolder.food_price.setText(String.format("%s", model.getPrice().toString()));
                Picasso.get().load(model.getImage()).into(viewHolder.food_image);

                //Quick cart

                viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean isExists = new Database(getBaseContext()).checkFoodExists(adapter.getRef(position).getKey(), Constant.currentUser.getPhone());

                        if (!isExists) {
                            new Database(getBaseContext()).addToCart(new Order(
                                    Constant.currentUser.getPhone(),
                                    adapter.getRef(position).getKey(),
                                    model.getName(),
                                    "1",
                                    model.getPrice(),
                                    model.getImage()

                            ));
                        } else {
                            new Database(getBaseContext()).increaseCart(Constant.currentUser.getPhone(), adapter.getRef(position).getKey());
                        }
                        Toast.makeText(FoodListActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    }
                });

                //add favourites
                if (localDB.isFavourite(adapter.getRef(position).getKey(), Constant.currentUser.getPhone()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite);

                //click to share
                viewHolder.share_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* Picasso.get()
                                .load(model.getImage())
                                .into(target);*/
                    }
                });

                //click to change the status of favourites
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Favorites favorites = new Favorites();
                        favorites.setFoodId(adapter.getRef(position).getKey());
                        favorites.setFoodName(model.getName());
                        favorites.setFoodDescription(model.getDescription());
                        favorites.setFoodImage(model.getImage());
                        favorites.setFoodMenuId(model.getMenuId());
                        favorites.setUserPhone(Constant.currentUser.getPhone());
                        favorites.setFoodPrice(model.getPrice());

                        if (!localDB.isFavourite(adapter.getRef(position).getKey(), Constant.currentUser.getPhone())) {

                            localDB.addToFavourites(favorites);
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite);
                            Toast.makeText(FoodListActivity.this, "" + model.getName() +
                                    " was added to Favourites", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromFavourites(adapter.getRef(position).getKey(), Constant.currentUser.getPhone());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border);
                            Toast.makeText(FoodListActivity.this, "" + model.getName() +
                                    " was removed from Favourites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //start new activity
                        Intent foodDetail = new Intent(FoodListActivity.this, FoodDetailsActivity.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey()); //send FoodId to new activity
                        startActivity(foodDetail);
                    }
                });
            }

            @NonNull
            @Override
            public com.example.eatathome.Client.ViewHolder.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);
                return new com.example.eatathome.Client.ViewHolder.FoodViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerViewFood.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}