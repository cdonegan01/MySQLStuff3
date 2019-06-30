package com.example.mysqlstuff.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.R;
import com.example.mysqlstuff.ReviewActivity;
import com.example.mysqlstuff.model.Review;

import java.util.List;

public class ReviewAdapter3 extends RecyclerView.Adapter<ReviewAdapter3.MyViewHolder> {

    private Context mContext;
    private List<Review> mData;
    RequestOptions options;

    public ReviewAdapter3(Context mContext, List<Review> mData) {
        this.mContext = mContext;
        this.mData = mData;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);

    }

    @NonNull
    @Override
    public ReviewAdapter3.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.activity_feed,viewGroup, false);
        final ReviewAdapter3.MyViewHolder viewHolder = new ReviewAdapter3.MyViewHolder(view);
        return new ReviewAdapter3.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewAdapter3.MyViewHolder myViewHolder, int i) {

        myViewHolder.feedNameAndGame.setText(mData.get(i).getGameName()+" review by "+mData.get(i).getAuthorName());
        myViewHolder.feedRating.setText(mData.get(i).getRating()+"/10");
        myViewHolder.feedReview.setText(mData.get(i).getReview());
        Glide.with(mContext).load(mData.get(i).getGamePictureUrl()).apply(options).into(myViewHolder.feedThumbnail);
        myViewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, ReviewActivity.class);
                i.putExtra("gameTitle",mData.get(myViewHolder.getAdapterPosition()).getGameName());
                i.putExtra("review",mData.get(myViewHolder.getAdapterPosition()).getReview());
                i.putExtra("reviewId",mData.get(myViewHolder.getAdapterPosition()).getReviewId());
                i.putExtra("authorID",mData.get(myViewHolder.getAdapterPosition()).getAuthorId());
                i.putExtra("authorName",mData.get(myViewHolder.getAdapterPosition()).getAuthorName());
                i.putExtra("authorPicture",mData.get(myViewHolder.getAdapterPosition()).getAuthorPictureUrl());
                i.putExtra("gameId",mData.get(myViewHolder.getAdapterPosition()).getGameId());
                i.putExtra("gamePicture",mData.get(myViewHolder.getAdapterPosition()).getGamePictureUrl());
                i.putExtra("likes",mData.get(myViewHolder.getAdapterPosition()).getLikes());
                i.putExtra("rating",mData.get(myViewHolder.getAdapterPosition()).getRating());

                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView feedNameAndGame;
        TextView feedRating;
        TextView feedReview;
        ImageView feedThumbnail;
        LinearLayout view_container;

        public MyViewHolder (View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            feedNameAndGame = itemView.findViewById(R.id.feedNameAndGame);
            feedReview = itemView.findViewById(R.id.feedReview);
            feedRating = itemView.findViewById(R.id.feedRating);
            feedThumbnail = itemView.findViewById(R.id.feedThumbnail);

        }
    }
}
