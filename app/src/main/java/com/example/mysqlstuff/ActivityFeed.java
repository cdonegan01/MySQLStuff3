package com.example.mysqlstuff;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
    private User user = new User();

    /**
     * Declaring Vars and Lists
     */

    private String followList_url = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/followList2.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Review> reviewList;
    private RecyclerView recyclerView;

    /**
     * Instantiating Methods for Navigation Menu
     * Each case represents one of the five options on the bottom menu, taking the user to the
     * corresponding page. The exception to this is the nav_logout case, which first uses the
     * session.logoutUser method to remove the user's information from the Shared Preferences
     * before navigating them back to the Login Screen.
     */

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {
                case R.id.nav_activity:
                    i = new Intent(getApplicationContext(), ActivityFeed.class);
                    startActivity(i);
                    break;
                case R.id.nav_gameSearch:
                    i = new Intent(getApplicationContext(), GameListActivity.class);
                    startActivity(i);
                    break;
                case R.id.nav_userPage:
                    i = new Intent(getApplicationContext(), UserPageActivity.class);
                    startActivity(i);
                    break;
                case R.id.nav_userSearch:
                    i = new Intent(getApplicationContext(), UserListActivity.class);
                    startActivity(i);
                    break;
                case R.id.nav_logout:
                    session.logoutUser();
                    Intent logout = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(logout);
                    finish();
                    break;
            }
            return false;
        }
    };

    /**
     * Constructs the current page, assigning all View Vars and calling all methods needed
     * to display data to the user
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        session = new Session(getApplicationContext());
        if (session.isLoggedIn() == false) {
            Intent logout = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(logout);
            finish();
        }
        User user = session.getUserDetails();
        String greeting = "Hey there, "+user.getUsername();
        TextView userIntroID = findViewById(R.id.userIntroID);
        userIntroID.setText(greeting);
        reviewList = new ArrayList<>();
        recyclerView = findViewById(R.id.activityFeedRVId);
        jsoncall(user);

    }

    /**
     * Retrieves data from the database and assigns that to an array of type "Review", then calls the
     * setRVadapter method for this class.
     * @param user
     */

    private void jsoncall(final User user) {
        ArrayRequest = new JsonArrayRequest(followList_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("User") == user.getUserId()) {
                            Review review = new Review();
                            review.setReviewId(jsonObject.getInt("Review_id"));
                            review.setGameName(jsonObject.getString("title"));
                            review.setAuthorName(jsonObject.getString("Username"));
                            review.setAuthorPictureUrl(jsonObject.getString("Avatar"));
                            review.setGamePictureUrl(jsonObject.getString("cover_art"));
                            review.setLikes(jsonObject.getString("Likes"));
                            review.setRating(jsonObject.getString("Rating"));
                            review.setReview(jsonObject.getString("Review"));
                            review.setHeading(jsonObject.getString("Heading"));
                            review.setAuthorId(jsonObject.getInt("user_id"));
                            review.setGameId(jsonObject.getInt("id"));
                            reviewList.add(review);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (reviewList.isEmpty()) {
                    TextView textView = findViewById(R.id.textView3);
                    textView.setText("You aren't following anyone yet!");
                }
                setRvadapter(reviewList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(ActivityFeed.this);
        requestQueue.add(ArrayRequest);
    }

    /**
     * Applies the corresponding layout file to the RecyclerView element
     * with the corresponding adapter class.
     * @param reviewList
     */

    public void setRvadapter (List<Review> reviewList) {
        ReviewAdapter3 myAdapter = new ReviewAdapter3(this,reviewList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }
}
