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
import com.example.mysqlstuff.adapter.GameAdapter;
import com.example.mysqlstuff.objects.Game;
import com.example.mysqlstuff.objects.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameListActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declaring Vars and Lists
     */

    private Session session;

    private String jsonURL1 = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/gamelist.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Game> gameList;
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
        setContentView(R.layout.activity_game_list);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        session = new Session(getApplicationContext());
        if (session.isLoggedIn() == false) {
            Intent logout = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(logout);
            finish();
        }
        final User user = session.getUserDetails();
        gameList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv);
        jsoncall();

        ConstraintLayout constraintLayout3 = findViewById(R.id.linearLayout);
        constraintLayout3.setOnClickListener(this);

    }

    /**
     * Retrieves data from the database and assigns that to an array of type "Game", then calls the
     * setRVadapter method for this class.
     */

    private void jsoncall() {
        ArrayRequest = new JsonArrayRequest(jsonURL1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Game game = new Game();
                        game.setGameId(jsonObject.getInt("id"));
                        game.setTitle(jsonObject.getString("title"));
                        game.setReleaseYear(jsonObject.getString("release_year"));
                        game.setDeveloper(jsonObject.getString("developer"));
                        game.setDescription(jsonObject.getString("description"));
                        game.setImage_url(jsonObject.getString("cover_art"));
                        gameList.add(game);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setRvadapter(gameList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(GameListActivity.this);
        requestQueue.add(ArrayRequest);
    }

    /**
     * Applies the corresponding layout file to the RecyclerView element
     * with the corresponding adapter class.
     * @param gameList
     */

    public void setRvadapter (List<Game> gameList) {

        GameAdapter myAdapter = new GameAdapter(this,gameList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }

    /**
     * Upon a user pressing the corresponding button, retrieves data from the database and assigns
     * that to an array of type "Review", then calls the setRVadapter method for this class. Only
     * games that match the search criteria are assigned to the array.
     * @param view
     */

    @Override
    public void onClick(View view) {
        gameList.clear();
        final EditText searchData = findViewById(R.id.gameListSearchBar);
        final String searchInfo = searchData.getText().toString();
        ArrayRequest = new JsonArrayRequest(jsonURL1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (searchData == null) {
                    for (int i = 0 ; i<response.length();i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            Game game = new Game();
                            game.setGameId(jsonObject.getInt("id"));
                            game.setTitle(jsonObject.getString("title"));
                            game.setReleaseYear(jsonObject.getString("release_year"));
                            game.setDeveloper(jsonObject.getString("developer"));
                            game.setDescription(jsonObject.getString("description"));
                            game.setImage_url(jsonObject.getString("cover_art"));
                            gameList.add(game);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getString("title").equalsIgnoreCase(searchInfo) || jsonObject.getString("title").toLowerCase().contains(searchInfo.toLowerCase()) ) {
                            Game game = new Game();
                            game.setGameId(jsonObject.getInt("id"));
                            game.setTitle(jsonObject.getString("title"));
                            game.setReleaseYear(jsonObject.getString("release_year"));
                            game.setDeveloper(jsonObject.getString("developer"));
                            game.setDescription(jsonObject.getString("description"));
                            game.setImage_url(jsonObject.getString("cover_art"));
                            gameList.add(game);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                }
                setRvadapter(gameList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(GameListActivity.this);
        requestQueue.add(ArrayRequest);
    }
}
