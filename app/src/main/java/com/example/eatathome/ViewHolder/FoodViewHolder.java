package com.example.eatathome.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatathome.Constant.Constant;
import com.example.eatathome.Interface.ItemClickListener;
import com.example.eatathome.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener{

    public TextView txtFoodName;
    public ImageView foodImage, fav_image;

    private ItemClickListener itemClickListener;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        txtFoodName = itemView.findViewById(R.id.food_name);
        foodImage = itemView.findViewById(R.id.food_image);
        fav_image = itemView.findViewById(R.id.iv_fav);

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
