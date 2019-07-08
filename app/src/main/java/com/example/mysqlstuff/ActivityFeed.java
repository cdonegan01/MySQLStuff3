package com.example.mysqlstuff;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.adapter.ReviewAdapter3;
import com.example.mysqlstuff.objects.Review;
import com.example.mysqlstuff.objects.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityFeed extends AppCompatActivity {

    private Session session;

    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/reviewList.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Review> lstReviews;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        session = new Session(getApplicationContext());
        User user = session.getUserDetails();
        String greeting = "Hey there, "+user.getUsername();
        TextView userIntroID = findViewById(R.id.userIntroID);
        userIntroID.setText(greeting);
        lstReviews = new ArrayList<>();
        recyclerView = findViewById(R.id.activityFeedRVId);
        jsoncall(user.getUsername());
    }

    private void jsoncall(String username) {
        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
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
        requestQueue = Volley.newRequestQueue(ActivityFeed.this);
        requestQueue.add(ArrayRequest);
    }

    public void setRvadapter (List<Review> lstReviews) {
        ReviewAdapter3 myAdapter = new ReviewAdapter3(this,lstReviews) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }
}
