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
import com.example.mysqlstuff.objects.Review;

import java.util.List;

public class ReviewAdapter2 extends RecyclerView.Adapter<ReviewAdapter2.MyViewHolder> {

    /**
     * Declaring Vars
     */

    private Context context;
    private List<Review> reviews;
    RequestOptions options;

    public ReviewAdapter2(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);

    }

    /**
     * Creates a view that holds the layout for the Review List on User Pages
     * @param viewGroup
     * @param i
     * @return
     */

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.review_list_user_layout,viewGroup, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    /**
     * Applies the parameters of the Review object to the created viewholder and prepares the Intent for user interaction.
     * @param myViewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.gameNameTextView.setText(reviews.get(i).getGameName());
        myViewHolder.ratingTextView.setText(reviews.get(i).getRating()+"/10");
        myViewHolder.reviewTextView.setText(reviews.get(i).getHeading());
        Glide.with(context).load(reviews.get(i).getGamePictureUrl()).apply(options).into(myViewHolder.gamePicture);
        myViewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ReviewActivity.class);
                i.putExtra("gameTitle", reviews.get(myViewHolder.getAdapterPosition()).getGameName());
                i.putExtra("review", reviews.get(myViewHolder.getAdapterPosition()).getReview());
                i.putExtra("reviewId", reviews.get(myViewHolder.getAdapterPosition()).getReviewId());
                i.putExtra("authorID", reviews.get(myViewHolder.getAdapterPosition()).getAuthorId());
                i.putExtra("authorName", reviews.get(myViewHolder.getAdapterPosition()).getAuthorName());
                i.putExtra("authorPicture", reviews.get(myViewHolder.getAdapterPosition()).getAuthorPictureUrl());
                i.putExtra("gameId", reviews.get(myViewHolder.getAdapterPosition()).getGameId());
                i.putExtra("gamePicture", reviews.get(myViewHolder.getAdapterPosition()).getGamePictureUrl());
                i.putExtra("likes", reviews.get(myViewHolder.getAdapterPosition()).getLikes());
                i.putExtra("rating", reviews.get(myViewHolder.getAdapterPosition()).getRating());
                i.putExtra("heading", reviews.get(myViewHolder.getAdapterPosition()).getHeading());

                context.startActivity(i);
            }
        });

    }

    /**
     * Returns the size of the Review arraylist
     * @return
     */

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    /**
     * Creates the view elements for each variable and assigns them to itemView objects in the layout file.
     */

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView gameNameTextView;
        TextView ratingTextView;
        TextView reviewTextView;
        ImageView gamePicture;
        LinearLayout view_container;

        public MyViewHolder (View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            gameNameTextView = itemView.findViewById(R.id.gameNameUser);
            reviewTextView = itemView.findViewById(R.id.reviewUser);
            ratingTextView = itemView.findViewById(R.id.ratingUserReview);
            gamePicture = itemView.findViewById(R.id.thumbnailUserReview);

        }
    }

}
