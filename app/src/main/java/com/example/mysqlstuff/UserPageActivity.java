package com.example.mysqlstuff;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.mysqlstuff.objects.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserPageActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declaring Vars and Lists
     */

    private Session session;

    private String reviewList_url = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/reviewList.php";
    private String reviewListLikes_url = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/reviewListLikes.php";
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
        setContentView(R.layout.activity_user_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        session = new Session(getApplicationContext());
        if (session.isLoggedIn() == false) {
            Intent logout = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(logout);
            finish();
        }
        User user = session.getUserDetails();

        String name  = user.getUsername();
        String bio = user.getBio();
        String image_url = user.getProfilePic_url();
        int followers = user.getFollowers();
        int authorID = user.getUserId();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.otherUserName);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView currentUserBio = findViewById(R.id.userPageBio);
        TextView currentUserFollowers = findViewById(R.id.userPageFollowers);
        ImageView currentUserProfile = findViewById(R.id.userPageAvatar);

        currentUserBio.setText(bio);
        currentUserFollowers.setText(followers+" Followers");
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.ic_account_box_black_24dp).error(R.drawable.ic_account_box_black_24dp);
        Glide.with(this).load(image_url).apply(requestOptions).into(currentUserProfile);

        collapsingToolbarLayout.setTitle(name);

        ConstraintLayout constraintLayout1 = findViewById(R.id.constraintLayoutUser);
        constraintLayout1.setOnClickListener(this);

        reviewList = new ArrayList<>();
        recyclerView = findViewById(R.id.userPageReviews);
        jsoncall();
    }

    /**
     * Retrieves data from the database, stores data in an object of type
     * "Review" assigns objects to an array of that type, then calls the
     * setRVadapter method for this class. Only reviews written by the
     * current user are found here.
     */

    private void jsoncall() {
        User user = session.getUserDetails();
        final int authorID = user.getUserId();
        ArrayRequest = new JsonArrayRequest(reviewList_url, new Response.Listener<JSONArray>() {
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
                setRvadapter(reviewList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(UserPageActivity.this);
        requestQueue.add(ArrayRequest);
    }

    /**
     * Upon user tapping the corresponding button, retrieves data from the database and assigns
     * that to an array of type "Review", then calls the setRVadapter method for this class.
     * Only reviews written by the current user are added to the array. Results are
     * ordered from newest to oldest.
     * @param view
     */

    public void jsoncallOtherUserRecent(View view) {
        reviewList.clear();
        User user = session.getUserDetails();
        final int authorID = user.getUserId();
        ArrayRequest = new JsonArrayRequest(reviewList_url, new Response.Listener<JSONArray>() {
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
                            reviewList.add(review);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setRvadapter(reviewList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(UserPageActivity.this);
        requestQueue.add(ArrayRequest);
    }

    /**
     * Upon user tapping the corresponding button, retrieves data from the database and assigns
     * that to an array of type "Review", then calls the setRVadapter method for this class.
     * Only reviews written by the current user are added to the array. Results are
     * ordered from most likes to least likes.
     * @param view
     */

    public void jsoncallOtherUserPopular(View view) {
        reviewList.clear();
        User user = session.getUserDetails();
        final int authorID = user.getUserId();
        ArrayRequest = new JsonArrayRequest(reviewListLikes_url, new Response.Listener<JSONArray>() {
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
                            reviewList.add(review);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setRvadapter(reviewList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(UserPageActivity.this);
        requestQueue.add(ArrayRequest);
    }

    /**
     * Applies the corresponding layout file to the RecyclerView element
     * with the corresponding adapter class.
     * @param reviewList
     */

    public void setRvadapter (List<Review> reviewList) {
        ReviewAdapter2 myAdapter = new ReviewAdapter2(this,reviewList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

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
