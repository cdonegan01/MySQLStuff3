package com.example.mysqlstuff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.adapter.CommentAdapter;
import com.example.mysqlstuff.objects.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/commentList.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Comment> lstComments;
    private RecyclerView recyclerView;

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

        tv_review.setMovementMethod(new ScrollingMovementMethod());

        tv_rating.setText(rating+"/10");
        tv_likes.setText(likes);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);
        // set image using Glide
        Glide.with(this).load(image_url).apply(requestOptions).into(profilePicImg);
        Glide.with(this).load(gameImage).apply(requestOptions).into(gamePicImg);

        lstComments = new ArrayList<>();
        recyclerView = findViewById(R.id.commendsRvID);

        jsoncall();

    }

    private void jsoncall() {
        final int reviewID = getIntent().getExtras().getInt("reviewId");
        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("Review") == reviewID)  {
                            Comment comment = new Comment();
                            comment.setComment(jsonObject.getString("Comment"));
                            comment.setUserAvatarURL(jsonObject.getString("Avatar"));
                            comment.setUserName(jsonObject.getString("Username"));
                            comment.setUserID(jsonObject.getString("user_id"));
                            lstComments.add(comment);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setRvadapter(lstComments);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(ReviewActivity.this);
        requestQueue.add(ArrayRequest);
    }

    public void setRvadapter (List<Comment> lstComments) {
        CommentAdapter myAdapter = new CommentAdapter(this,lstComments) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }
}
