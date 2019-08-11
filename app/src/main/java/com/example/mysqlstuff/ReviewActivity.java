package com.example.mysqlstuff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.adapter.CommentAdapter;
import com.example.mysqlstuff.objects.Comment;
import com.example.mysqlstuff.objects.Constants;
import com.example.mysqlstuff.objects.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    /**
     * Declaring Vars and Lists
     */

    private Session session;
    private ProgressDialog pDialog;
    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/commentList.php";
    private String URL_JSON2 = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/likeList.php";
    private String reviewLikeURL = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/reviewLiker.php";
    private String commentPostURL = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/commentPoster.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Comment> lstComments;
    private RecyclerView recyclerView;
    private TextView commentField;
    private String currentUser;
    private String currentReview;
    private String comment;

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
        setContentView(R.layout.activity_review);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        session = new Session(getApplicationContext());
        if (session.isLoggedIn() == false) {
            Intent logout = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(logout);
            finish();
        }
        final User user = session.getUserDetails();

        String gameTitle  = getIntent().getExtras().getString("gameTitle");
        String review = getIntent().getExtras().getString("review");
        String authorName = getIntent().getExtras().getString("authorName");
        String rating = getIntent().getExtras().getString("rating");
        String image_url = getIntent().getExtras().getString("authorPicture");
        String gameImage = getIntent().getExtras().getString("gamePicture");
        final int reviewId = getIntent().getExtras().getInt("reviewId");

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

        likeGetter(reviewId);
        tv_rating.setText(rating+"/10");

        ImageView likeButton = findViewById(R.id.likeButtonID);
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.ic_account_box_black_24dp).error(R.drawable.ic_account_box_black_24dp);
        // set image using Glide
        Glide.with(this).load(image_url).apply(requestOptions).into(profilePicImg);
        Glide.with(this).load(gameImage).apply(requestOptions).into(gamePicImg);

        likeChecker(user.getUserId(), reviewId);

        Button commenter = findViewById(R.id.commentButton);

        commentField = findViewById(R.id.commentField);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeReview(user.getUserId(), reviewId);
                likeGetter(reviewId);
            }
        });

        commenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                currentUser = Integer.toString(user.getUserId());
                currentReview = Integer.toString(reviewId);
                comment = commentField.getText().toString();

                if (validateInputs() == true) {
                    postComment(currentUser, currentReview, comment);
                }

            }
        });

        lstComments = new ArrayList<>();
        recyclerView = findViewById(R.id.commendsRvID);

        jsoncall();

    }

    /**
     * Upon user tapping the corresponding button, retrieves data from the database and assigns
     * that to an array of type "Comment", then calls the setRVadapter method for this class.
     * Only comments that match the current "ReviewID" are added to the array.
     */

    private void jsoncall() {
        lstComments.clear();
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

    /**
     * Applies the corresponding layout file to the RecyclerView element
     * with the corresponding adapter class.
     * @param commentList
     */

    public void setRvadapter (List<Comment> commentList) {
        CommentAdapter myAdapter = new CommentAdapter(this,commentList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    /**
     * Adds a Like assigned to the current review to the database from the current user
     * @param currentUserID
     * @param reviewID
     */

    public void likeReview (int currentUserID, int reviewID) {
        String likerPost = Integer.toString(currentUserID);
        String reviewPost = Integer.toString(reviewID);
        new Backgroundworker(this).execute("userInteract", reviewLikeURL, likerPost, reviewPost);
        Toast.makeText(getApplicationContext(),
                "You have successfully liked this review!", Toast.LENGTH_SHORT).show();
        View a = findViewById(R.id.likeButtonID);
        a.setVisibility(View.GONE);
        View b = findViewById(R.id.likedButtonID);
        b.setVisibility(View.VISIBLE);
    }

    /**
     * Checks to make sure the current user has not already liked the current review
     * @param currentUserID
     * @param reviewId
     */

    public void likeChecker (final int currentUserID, final int reviewId) {
        ArrayRequest = new JsonArrayRequest(URL_JSON2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                int counter = 0;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("Liker") == currentUserID && jsonObject.getInt("Review") == reviewId) {
                            counter++;
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (counter > 0) {
                    View a = findViewById(R.id.likeButtonID);
                    a.setVisibility(View.GONE);
                    View b = findViewById(R.id.likedButtonID);
                    b.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(ReviewActivity.this);
        requestQueue.add(ArrayRequest);

    }

    /**
     * Retrieves the number of likes the current review has from the database
     * @param reviewId
     */

    public void likeGetter (final int reviewId) {
        ArrayRequest = new JsonArrayRequest(URL_JSON2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                int counter = 0;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("Review") == reviewId) {
                            counter++;
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                String finalLikes = Integer.toString(counter);

                TextView tv_likes = findViewById(R.id.likeCounterID);

                tv_likes.setText(finalLikes+" Likes");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(ReviewActivity.this);
        requestQueue.add(ArrayRequest);

    }

    /**
     * Posts a comment to the database for the current review
     * @param author
     * @param review
     * @param comment
     */

    public void postComment(String author, String review, String comment) {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put(Constants.COMMENT, comment);
            request.put("User", author);
            request.put("Review", review);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, commentPostURL, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt(Constants.STATUS) == 0) {
                                Toast.makeText(getApplicationContext(),
                                        "Comment Posted!", Toast.LENGTH_LONG).show();
                                jsoncall();
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Displays Progress bar while adding comment
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(ReviewActivity.this);
        pDialog.setMessage("Adding Comment...Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Validates inputs and shows error if any exist
     * @return
     */

    private boolean validateInputs() {
        if (Constants.NULL.equals(commentField)) {
            commentField.setError("Must type a Comment!");
            commentField.requestFocus();
            return false;
        }
        return true;
    }
}
