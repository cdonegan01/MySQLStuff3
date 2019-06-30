package com.example.mysqlstuff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysqlstuff.adapter.UserAdapter;
import com.example.mysqlstuff.objects.otherUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private String URL_JSON = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/userlist.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<otherUser> lstUser;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        lstUser = new ArrayList<>();
        recyclerView = findViewById(R.id.userList);
        jsoncall();
    }

    private void jsoncall() {
        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0 ; i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        otherUser otherUser = new otherUser();
                        otherUser.setOtherUserId(jsonObject.getInt("user_id"));
                        otherUser.setOtherEmail(jsonObject.getString("Email"));
                        otherUser.setOtherUsername(jsonObject.getString("Username"));
                        otherUser.setOtherName(jsonObject.getString("Name"));
                        otherUser.setOtherLocation(jsonObject.getString("Location"));
                        otherUser.setOtherBio(jsonObject.getString("Bio"));
                        otherUser.setOtherPronoun(jsonObject.getString("Pronoun"));
                        otherUser.setOtherProfilePic_url(jsonObject.getString("Avatar"));
                        otherUser.setOtherFollowers(jsonObject.getInt("Followers"));
                        lstUser.add(otherUser);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setRvadapter(lstUser);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(UserListActivity.this);
        requestQueue.add(ArrayRequest);
    }

    public void setRvadapter (List<otherUser> lstGame) {

        UserAdapter myAdapter = new UserAdapter(this,lstGame) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }
}


