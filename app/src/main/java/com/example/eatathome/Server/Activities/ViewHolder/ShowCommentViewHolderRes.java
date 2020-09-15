package com.example.eatathome.Server.Activities.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eatathome.R;

public class ShowCommentViewHolderRes extends RecyclerView.ViewHolder {

    public TextView txtUserPhone, txtComment, txtFoodName;
    public RatingBar ratingBar;
    public ImageView commentImage;
    public Button btnDeleteComment;

    public ShowCommentViewHolderRes(View itemView) {
        super(itemView);
        txtComment = (TextView)itemView.findViewById(R.id.comment);
        txtUserPhone = (TextView)itemView.findViewById(R.id.comment_user_phone);
        txtFoodName = (TextView)itemView.findViewById(R.id.comment_item_name);
        ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
        commentImage = (ImageView)itemView.findViewById(R.id.comment_image);
        btnDeleteComment = (Button)itemView.findViewById(R.id.btnDeleteComment);
    }
}
