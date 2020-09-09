package com.example.eatathome.Client.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatathome.Client.Activities.Services.ListenOrder;
import com.example.eatathome.Constant.Constant;
import com.example.eatathome.Interface.ItemClickListener;
import com.example.eatathome.Models.Restaurant;
import com.example.eatathome.R;
import com.example.eatathome.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class RestaurantListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerView;
    TextView textFullName;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter;
    FirebaseRecyclerOptions<Restaurant> firebaseRecyclerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Restaurants");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(RestaurantListActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name for user
        final View headerView = navigationView.getHeaderView(0);
        textFullName = headerView.findViewById(R.id.text_full_name);
        textFullName.setText(Constant.currentUser.getName());


        //init firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Restaurants");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Restaurant>().setQuery(category, Restaurant.class).build();

        //paper init
        Paper.init(this);

        recyclerView = findViewById(R.id.recyclerview_menu1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (Constant.isConnectedToInternet(this))
            loadRestaurant();
        else {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        //load services
        Intent services = new Intent(RestaurantListActivity.this, ListenOrder.class);
        startService(services);

    }


    private void loadRestaurant() {

        adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
                return new RestaurantViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull Restaurant model) {
                holder.restaurantName.setText(model.getName());
                Picasso.get().load(model.getImage())
                        .into(holder.restaurantImage);

                final Restaurant clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodList = new Intent(RestaurantListActivity.this, RestaurantSubCategoriesActivity.class);
                        Constant.restaurantSelected = adapter.getRef(position).getKey();
                        startActivity(foodList);
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==R.id.refresh)
            loadRestaurant();
        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(RestaurantListActivity.this, CartActivity.class);
            startActivity(cartIntent);

        } else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(RestaurantListActivity.this, OrderStatusActivity.class);
            startActivity(orderIntent);

        } else if (id == R.id.nav_sign_out) {
            Paper.book().destroy();
            Intent signIn = new Intent(RestaurantListActivity.this, SignInActivity.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}