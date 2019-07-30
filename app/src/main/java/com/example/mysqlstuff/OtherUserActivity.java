package com.example.mysqlstuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class OtherUserActivity  extends AppCompatActivity implements View.OnClickListener {

    private Session session;

    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/reviewList.php";
    private String URL_JSON2 = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/reviewListLikes.php";
    private String URL_JSON3 = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/followList.php";
    private String URL_JSON4 = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/helpfulList.php";
    private String follow_url = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/userFollow0.php";
    private String helpful_url = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/helpfulUser.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Review> lstReviews;
    private RecyclerView recyclerView;
    private TextView otherFollowers;
    private TextView otherHelpful;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        session = new Session(getApplicationContext());
        if (session.isLoggedIn() == false) {
            Intent logout = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(logout);
            finish();
        }
        final User user = session.getUserDetails();
        String name  = getIntent().getExtras().getString("otherUsername");
        String bio = getIntent().getExtras().getString("otherBio");
        String reviewTitle = "Critiques by "+name;
        String image_url = getIntent().getExtras().getString("otherProfilePic");
        String followers = getIntent().getExtras().getString("otherFollowers")+" Followers";
        //String helpful = "Helpful Score: "+getIntent().getExtras().getString("otherHelpful");
        final int otherUserId = getIntent().getExtras().getInt("otherUserId");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.otherUserName);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView otherUserBio = findViewById(R.id.otherUserBio);
        TextView otherReviewsTitle = findViewById(R.id.otherReviewsTitle);
        otherFollowers = findViewById(R.id.otherUserFollowers);
        ImageView otherUserProfile = findViewById(R.id.otherUserProfile);
        otherHelpful = findViewById(R.id.otherUserHelpfulRating);

        Button followButton = findViewById(R.id.followButton);

        Button helpfulButton = findViewById(R.id.helpfulButton);

        otherUserBio.setText(bio);
        otherReviewsTitle.setText(reviewTitle);
        otherFollowers.setText(followers);
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.ic_account_box_black_24dp).error(R.drawable.ic_account_box_black_24dp);
        Glide.with(this).load(image_url).apply(requestOptions).into(otherUserProfile);

        collapsingToolbarLayout.setTitle(name);

        ConstraintLayout constraintLayout1 = findViewById(R.id.constraingLayoutOtherUser);
        constraintLayout1.setOnClickListener(this);

        followChecker(user.getUserId(), otherUserId);
        helpfulChecker(user.getUserId(), otherUserId);

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUser(user.getUserId(), otherUserId);
            }
        });

        helpfulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpfulUser(user.getUserId(), otherUserId);
            }
        });

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
                            review.setHeading(jsonObject.getString("Heading"));
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

    public void jsoncallOtherUserRecent(View view) {
        lstReviews.clear();
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

    public void jsoncallOtherUserPopular(View view) {
        lstReviews.clear();
        final int authorID = getIntent().getExtras().getInt("otherUserId");
        ArrayRequest = new JsonArrayRequest(URL_JSON2, new Response.Listener<JSONArray>() {
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

    public void followUser (int currentUserID, int followedUserID) {
        String currentPost = Integer.toString(currentUserID);
        String followPost = Integer.toString(followedUserID);
        new Backgroundworker(this).execute("userInteract", follow_url, currentPost, followPost);
        Toast.makeText(getApplicationContext(),
                "You are now following this user!", Toast.LENGTH_SHORT).show();
        View a = findViewById(R.id.followButton);
        a.setVisibility(View.GONE);
        View b = findViewById(R.id.textViewFollowed);
        b.setVisibility(View.VISIBLE);
    }

    public void helpfulChecker (final int currentUserID, final int followedUserID) {
        ArrayRequest = new JsonArrayRequest(URL_JSON4, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                int counter = 0;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("Liking_User") == currentUserID && jsonObject.getInt("Liked_User") == followedUserID) {
                            counter++;
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (counter > 0) {
                    otherHelpful.setText("Helpful Rating: "+counter);
                    View a = findViewById(R.id.helpfulButton);
                    a.setVisibility(View.GONE);
                    View b = findViewById(R.id.helpfulUserTV);
                    b.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(OtherUserActivity.this);
        requestQueue.add(ArrayRequest);

    }

    public void helpfulUser (int currentUserID, int followedUserID) {
        String currentPost = Integer.toString(currentUserID);
        String followPost = Integer.toString(followedUserID);
        new Backgroundworker(this).execute("userInteract", helpful_url, currentPost, followPost);
        Toast.makeText(getApplicationContext(),
                "You have marked this user as helpful!", Toast.LENGTH_SHORT).show();
        helpfulChecker(currentUserID, followedUserID);
        View a = findViewById(R.id.helpfulButton);
        a.setVisibility(View.GONE);
        View b = findViewById(R.id.helpfulUserTV);
        b.setVisibility(View.VISIBLE);
    }

    public void followChecker (final int currentUserID, final int followedUserID) {
        ArrayRequest = new JsonArrayRequest(URL_JSON3, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                int counter = 0;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("User") == currentUserID && jsonObject.getInt("FollowedUser") == followedUserID) {
                            counter++;
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (counter > 0) {
                    otherFollowers.setText("Followers: "+counter);
                    View a = findViewById(R.id.followButton);
                    a.setVisibility(View.GONE);
                    View b = findViewById(R.id.textViewFollowed);
                    b.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(OtherUserActivity.this);
        requestQueue.add(ArrayRequest);

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
