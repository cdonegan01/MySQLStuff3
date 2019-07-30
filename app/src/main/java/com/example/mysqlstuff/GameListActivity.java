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

    private Session session;

    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/gamelist.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Game> lstGame;
    private RecyclerView recyclerView;

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
        lstGame = new ArrayList<>();
        recyclerView = findViewById(R.id.rv);
        jsoncall();

        ConstraintLayout constraintLayout3 = findViewById(R.id.linearLayout);
        constraintLayout3.setOnClickListener(this);

    }

    private void jsoncall() {
        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
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
                        game.setAverageRating(jsonObject.getString("average_rating"));
                        game.setDescription(jsonObject.getString("description"));
                        game.setImage_url(jsonObject.getString("cover_art"));
                        lstGame.add(game);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setRvadapter(lstGame);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(GameListActivity.this);
        requestQueue.add(ArrayRequest);
    }

    public void setRvadapter (List<Game> lstGame) {

        GameAdapter myAdapter = new GameAdapter(this,lstGame) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onClick(View view) {
        lstGame.clear();
        final EditText searchData = findViewById(R.id.gameListSearchBar);
        final String searchInfo = searchData.getText().toString();
        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
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
                            game.setAverageRating(jsonObject.getString("average_rating"));
                            game.setDescription(jsonObject.getString("description"));
                            game.setImage_url(jsonObject.getString("cover_art"));
                            lstGame.add(game);
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
                            game.setAverageRating(jsonObject.getString("average_rating"));
                            game.setDescription(jsonObject.getString("description"));
                            game.setImage_url(jsonObject.getString("cover_art"));
                            lstGame.add(game);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                }
                setRvadapter(lstGame);
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
