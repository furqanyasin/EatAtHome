package com.example.eatathome.Client.Activities.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatathome.Client.Activities.Constant.Constant;
import com.example.eatathome.Interface.ItemClickListener;
import com.example.eatathome.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener{

    public TextView restaurantName;
    public ImageView restaurantImage;

    private ItemClickListener itemClickListener;

    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);

        restaurantName = itemView.findViewById(R.id.restaurant_name);
        restaurantImage = itemView.findViewById(R.id.restaurant_image);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Constant.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Constant.DELETE);

    }
}