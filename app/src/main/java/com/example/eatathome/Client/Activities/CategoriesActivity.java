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

import com.example.eatathome.Interface.ItemClickListener;
import com.example.eatathome.Client.Activities.Model.Category;
import com.example.eatathome.R;
import com.example.eatathome.Client.Activities.ViewHolder.MenuViewHolder;
import com.example.eatathome.Client.Activities.Constant.Constant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoriesActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    FirebaseRecyclerOptions<Category> firebaseRecyclerOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Food Categories");
        setSupportActionBar(toolbar);

        //init firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Restaurants").child(Constant.restaurantSelected).child("detail").child("Category");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Category>().setQuery(category, Category.class).build();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(CategoriesActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });

        //Load menu
        recyclerMenu = findViewById(R.id.recyclerview_menu1);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerMenu.setLayoutManager(layoutManager);

        if (Constant.isConnectedToInternet(this))
            loadMenu();
        else {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder holder, int position, @NonNull Category model) {
                holder.txtMenuName.setText(model.getName());
                Picasso.get().load(model.getImage())
                        .into(holder.imageView);

                final Category clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(HomeActivity.this, "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                        //Get CategoryId and send to new activity
                        Intent foodList = new Intent(CategoriesActivity.this, FoodListActivity.class);
                        //Because CategoryId is a key, so we just key of this item
                        foodList.putExtra(Constant.CATEGORY_ID, adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
        adapter.startListening();
    }
}