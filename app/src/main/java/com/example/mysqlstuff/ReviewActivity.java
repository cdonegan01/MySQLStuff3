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

        String name  = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");
        String studio = getIntent().getExtras().getString("developer") ;
        String category = getIntent().getExtras().getString("release_year");
        String rating = getIntent().getExtras().getString("average_rating");
        String image_url = getIntent().getExtras().getString("cover_art");

        int gameId = getIntent().getExtras().getInt("id");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView tv_name = findViewById(R.id.aa_title);
        TextView tv_developer = findViewById(R.id.aa_developer);
        TextView tv_releaseYear = findViewById(R.id.aa_release_year) ;
        TextView tv_description = findViewById(R.id.aa_description);
        TextView tv_rating  = findViewById(R.id.aa_rating) ;
        ImageView img = findViewById(R.id.aa_thumbnail);

        tv_name.setText(name);
        tv_releaseYear.setText(category);
        tv_description.setText(description);
        tv_rating.setText(rating);
        tv_developer.setText(studio);

        collapsingToolbarLayout.setTitle(name);


        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);


        // set image using Glide
        Glide.with(this).load(image_url).apply(requestOptions).into(img);
    }
}
