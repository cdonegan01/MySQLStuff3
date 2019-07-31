package com.example.mysqlstuff;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysqlstuff.adapter.UserAdapter;
import com.example.mysqlstuff.objects.User;
import com.example.mysqlstuff.objects.otherUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declaring Vars and Lists
     */

    private Session session;
    private User user = new User();

    private String userList_url = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/userlist.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue;
    private List<otherUser> otherUsers;
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
        setContentView(R.layout.activity_user_list);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        session = new Session(getApplicationContext());
        if (session.isLoggedIn() == false) {
            Intent logout = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(logout);
            finish();
        }
        User user = session.getUserDetails();
        otherUsers = new ArrayList<>();
        recyclerView = findViewById(R.id.userList);
        jsoncall(user);

        ConstraintLayout userListLayout = findViewById(R.id.userListLayout);
        userListLayout.setOnClickListener(this);
    }

    /**
     * Retrieves data from the database, stores data in an object of type "otherUser"
     * assigns objects to an array of that type, then calls the setRVadapter method for this class.
     * Code exists to ensure the current user is not visible in this list.
     * @param user
     */

    private void jsoncall(final User user) {
        ArrayRequest = new JsonArrayRequest(userList_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getInt("user_id") != user.getUserId()) {
                            otherUser otherUser = new otherUser();
                            otherUser.setOtherUserId(jsonObject.getInt("user_id"));
                            otherUser.setOtherUsername(jsonObject.getString("Username"));
                            otherUser.setOtherBio(jsonObject.getString("Bio"));
                            otherUser.setOtherProfilePic_url(jsonObject.getString("Avatar"));
                            otherUser.setOtherFollowers(jsonObject.getInt("Followers"));
                            otherUsers.add(otherUser);
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setRvadapter(otherUsers);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(UserListActivity.this);
        requestQueue.add(ArrayRequest);
    }

    /**
     * Applies the corresponding layout file to the RecyclerView element
     * with the corresponding adapter class.
     * @param otherUserList
     */

    public void setRvadapter (List<otherUser> otherUserList) {

        UserAdapter myAdapter = new UserAdapter(this,otherUserList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }

    /**
     * Upon corresponding button being selected, Retrieves data from the database,
     * stores data in an object of type "otherUser" assigns objects to an array of
     * that type, then calls the setRVadapter method for this class.
     * Only users that match the search criteria are included in the array
     * if that data is filled, otherwise the method retrieves all data.
     * Code exists to ensure the current user is not visible in this list.
     * @param view
     */

    @Override
    public void onClick(View view) {
        otherUsers.clear();
        user = session.getUserDetails();
        final EditText searchData = findViewById(R.id.userListSearchBar);
        final String searchInfo = searchData.getText().toString();
        ArrayRequest = new JsonArrayRequest(userList_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (searchInfo == null) {
                    for (int i = 0 ; i<response.length();i++) {
                        try {
                            if (jsonObject.getInt("user_id") != user.getUserId()) {
                                otherUser otherUser = new otherUser();
                                otherUser.setOtherUserId(jsonObject.getInt("user_id"));
                                otherUser.setOtherUsername(jsonObject.getString("Username"));
                                otherUser.setOtherBio(jsonObject.getString("Bio"));
                                otherUser.setOtherProfilePic_url(jsonObject.getString("Avatar"));
                                otherUser.setOtherFollowers(jsonObject.getInt("Followers"));
                                otherUsers.add(otherUser);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    setRvadapter(otherUsers);
                } else {
                    for (int i = 0 ; i<response.length();i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            if (jsonObject.getString("Username").equalsIgnoreCase(searchInfo) || jsonObject.getString("Username").toLowerCase().contains(searchInfo.toLowerCase())) {
                                if (jsonObject.getInt("user_id") != user.getUserId()) {
                                    otherUser otherUser = new otherUser();
                                    otherUser.setOtherUserId(jsonObject.getInt("user_id"));
                                    otherUser.setOtherUsername(jsonObject.getString("Username"));
                                    otherUser.setOtherBio(jsonObject.getString("Bio"));
                                    otherUser.setOtherProfilePic_url(jsonObject.getString("Avatar"));
                                    otherUser.setOtherFollowers(jsonObject.getInt("Followers"));
                                    otherUsers.add(otherUser);
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    setRvadapter(otherUsers);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(UserListActivity.this);
        requestQueue.add(ArrayRequest);
    }
}


