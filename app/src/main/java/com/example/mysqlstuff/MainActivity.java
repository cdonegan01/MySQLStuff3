package com.example.mysqlstuff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";

    private EditText UsernameEt, PasswordEt;

    private String username, password;

    private ProgressDialog pDialog;
    private String login_url = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/login2.php";
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(getApplicationContext());

        if(session.isLoggedIn()){
            loadDashboard();
        }

        UsernameEt = (EditText)findViewById(R.id.usernameText);
        PasswordEt = (EditText)findViewById(R.id.passwordText);

        PasswordEt.setOnKeyListener(this);

        ConstraintLayout constraintLayout1 = (ConstraintLayout) findViewById(R.id.constraintLayout1);

        constraintLayout1.setOnClickListener(this);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        String transferUsername = UsernameEt.getText().toString();
        String transferPassword = PasswordEt.getText().toString();
        intent.putExtra("EX_USER", transferUsername);
        intent.putExtra("EX_PASS", transferPassword);
        startActivity(intent);

    }

    public void goToUserList(View view) {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    public void goToGameList(View view) {
        Intent intent = new Intent(this, GameListActivity.class);
        startActivity(intent);
    }

    public void OnLogin(View view) {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, username);
            request.put(KEY_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully

                            if (response.getInt(KEY_STATUS) == 0) {
                                session.loginUser(username,response.getString(KEY_EMAIL));
                                loadDashboard();

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

    public void onClick(View view) {
        if (view.getId() == R.id.constraintLayout1) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            OnLogin(v);
        }
        return false;
    }

    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), ActivityFeed.class);
        startActivity(i);
        finish();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private boolean validateInputs() {
        if(KEY_EMPTY.equals(username)){
            UsernameEt.setError("Username cannot be empty");
            UsernameEt.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            PasswordEt.setError("Password cannot be empty");
            PasswordEt.requestFocus();
            return false;
        }
        return true;
    }


}
