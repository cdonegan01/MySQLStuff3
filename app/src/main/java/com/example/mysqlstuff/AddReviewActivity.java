package com.example.mysqlstuff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AddReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        String name  = getIntent().getExtras().getString("title");
        String image_url = getIntent().getExtras().getString("cover_art");
        int gameId = getIntent().getExtras().getInt("id");

        TextView tv_name = findViewById(R.id.gameNameAddID);
        ImageView img = findViewById(R.id.addReviewGamePicID);

        tv_name.setText(name);
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);
        Glide.with(this).load(image_url).apply(requestOptions).into(img);

    }
}
