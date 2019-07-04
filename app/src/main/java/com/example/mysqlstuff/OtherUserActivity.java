package com.example.mysqlstuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.adapter.ReviewAdapter2;
import com.example.mysqlstuff.objects.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OtherUserActivity  extends AppCompatActivity implements View.OnClickListener {

    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/reviewList.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Review> lstReviews;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);


        String name  = getIntent().getExtras().getString("otherUsername");
        String bio = getIntent().getExtras().getString("otherBio");
        String reviewTitle = "Reviews by "+name;
        String image_url = getIntent().getExtras().getString("otherProfilePic");
        int authorID = getIntent().getExtras().getInt("author");

        TextView otherUserName = findViewById(R.id.otherUserName);
        TextView otherUserBio = findViewById(R.id.otherUserBio);
        TextView otherReviewsTitle = findViewById(R.id.otherReviewsTitle);
        ImageView otherUserProfile = findViewById(R.id.otherUserProfile);

        otherUserName.setText(name);
        otherUserBio.setText(bio);
        otherReviewsTitle.setText(reviewTitle);
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);
        Glide.with(this).load(image_url).apply(requestOptions).into(otherUserProfile);

        ConstraintLayout constraintLayout1 = findViewById(R.id.constraingLayoutOtherUser);
        constraintLayout1.setOnClickListener(this);

        lstReviews = new ArrayList<>();
        recyclerView = findViewById(R.id.otherUserReviews);
        jsoncall();
    }

    private void jsoncall() {
        final int authorID = getIntent().getExtras().getInt("otherUserId");
        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("Author") == authorID) {
                            Review review = new Review();
                            review.setReviewId(jsonObject.getInt("Review_id"));
                            review.setGameName(jsonObject.getString("title"));
                            review.setAuthorName(jsonObject.getString("Username"));
                            review.setAuthorPictureUrl(jsonObject.getString("Avatar"));
                            review.setGamePictureUrl(jsonObject.getString("cover_art"));
                            review.setLikes(jsonObject.getString("Likes"));
                            review.setRating(jsonObject.getString("Rating"));
                            review.setReview(jsonObject.getString("Review"));
                            review.setAuthorId(jsonObject.getInt("user_id"));
                            review.setGameId(jsonObject.getInt("id"));
                            lstReviews.add(review);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setRvadapter(lstReviews);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(OtherUserActivity.this);
        requestQueue.add(ArrayRequest);
    }

    public void setRvadapter (List<Review> lstReviews) {
        ReviewAdapter2 myAdapter = new ReviewAdapter2(this,lstReviews) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }

    public void goToUserReviews(View view) {
        Intent intent = new Intent(this, UserReviewsActivity.class);
        String name  = getIntent().getExtras().getString("otherUsername");
        int authorID = getIntent().getExtras().getInt("author");
        intent.putExtra("otherUsername", name);
        intent.putExtra("author", authorID);
        startActivity(intent);
    }

    public void goToUserList(View view) {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.constraingLayoutOtherUser) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
