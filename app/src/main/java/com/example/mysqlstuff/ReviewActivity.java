package com.example.mysqlstuff;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        String gameTitle  = getIntent().getExtras().getString("gameTitle");
        String review = getIntent().getExtras().getString("review");
        String authorName = getIntent().getExtras().getString("authorName");
        String rating = getIntent().getExtras().getString("rating");
        String image_url = getIntent().getExtras().getString("authorPicture");
        String gameImage = getIntent().getExtras().getString("gamePicture");
        String likes = getIntent().getExtras().getString("likes")+" Likes";


        int gameId = getIntent().getExtras().getInt("id");

        TextView tv_name = findViewById(R.id.gameTitleID);
        TextView tv_authorName = findViewById(R.id.authorNameID) ;
        TextView tv_review = findViewById(R.id.reviewText);
        TextView tv_rating  = findViewById(R.id.score);
        TextView tv_likes = findViewById(R.id.likeCounterID);
        ImageView profilePicImg = findViewById(R.id.reviewAuthorProfilePicID);
        ImageView gamePicImg = findViewById(R.id.gameThumbnailImageID);

        tv_name.setText(gameTitle);
        tv_authorName.setText(authorName);
        tv_review.setText(review);
        tv_rating.setText(rating+"/10");
        tv_likes.setText(likes);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);
        // set image using Glide
        Glide.with(this).load(image_url).apply(requestOptions).into(profilePicImg);
        Glide.with(this).load(gameImage).apply(requestOptions).into(gamePicImg);
    }
}
