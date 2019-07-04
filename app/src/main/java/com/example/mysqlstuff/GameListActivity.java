package com.example.mysqlstuff;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysqlstuff.adapter.RvAdapter;
import com.example.mysqlstuff.objects.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameListActivity extends AppCompatActivity implements View.OnClickListener {

    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/gamelist.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Game> lstGame;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
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

        RvAdapter myAdapter = new RvAdapter(this,lstGame) ;
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
