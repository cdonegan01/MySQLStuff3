package com.example.mysqlstuff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysqlstuff.objects.Review;
import com.example.mysqlstuff.objects.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AddReviewActivity extends AppCompatActivity {
    private Session session;

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_HEADING = "Heading";
    private static final String KEY_REVIEw = "Review";
    private static final String KEY_USERID = "Author";
    private static final String KEY_GAME = "Game";
    private static final String KEY_RATING = "Rating";
    private static final String KEY_EMPTY = "";
    private ProgressDialog pDialog;

    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/reviewPoster.php";
    private String author;
    private String review;
    private String game;
    private String rating;
    private String heading;
    private EditText reviewET;
    private EditText headingET;
    private int gameId;
    private Spinner spinner;

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
        setContentView(R.layout.activity_add_review);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        session = new Session(getApplicationContext());
        final User user = session.getUserDetails();

        String name  = getIntent().getExtras().getString("title");
        String image_url = getIntent().getExtras().getString("cover_art");
        gameId = getIntent().getExtras().getInt("id");

        TextView tv_name = findViewById(R.id.gameNameAddID);
        ImageView img = findViewById(R.id.addReviewGamePicID);
        reviewET = findViewById(R.id.reviewEnterID);
        headingET = findViewById(R.id.headingEnterID);

        Button submitButton = findViewById(R.id.submitButtonAddReview);
        spinner = findViewById(R.id.gameRatingPickerID);

        tv_name.setText(name);
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.ic_account_box_black_24dp).error(R.drawable.ic_account_box_black_24dp);
        Glide.with(this).load(image_url).apply(requestOptions).into(img);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                author = Integer.toString(user.getUserId());
                review = reviewET.getText().toString();
                game = Integer.toString(gameId);
                rating = spinner.getSelectedItem().toString();
                heading = headingET.getText().toString();
                postReview(author, review, game, rating, heading);
            }
        });

    }

    public void postReview(String author, String review, String game, String rating, String heading) {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERID, author);
            request.put(KEY_REVIEw, review);
            request.put(KEY_GAME, game);
            request.put(KEY_RATING, rating);
            request.put(KEY_HEADING, heading);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, URL_JSON, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                Toast.makeText(getApplicationContext(),
                                        "Review Posted! Head to your User Page to check it out!", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), UserPageActivity.class);
                                startActivity(i);

                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if username is already taken
                                Toast.makeText(getApplicationContext(),
                                        "Error!", Toast.LENGTH_LONG).show();
                                //Intent i = new Intent(getApplicationContext(), UserPageActivity.class);
                                // startActivity(i);

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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

    private void displayLoader() {
        pDialog = new ProgressDialog(AddReviewActivity.this);
        pDialog.setMessage("Adding Review...Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(review)) {
            reviewET.setError("Must type a review!");
            reviewET.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(heading)) {
            headingET.setError("Heading cannot be empty");
            headingET.requestFocus();
            return false;
        }
        return true;
    }

}
