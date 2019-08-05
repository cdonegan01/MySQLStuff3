package com.example.mysqlstuff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mysqlstuff.objects.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    /**
     * Declaring Vars and Lists
     */

    private EditText UsernameEt;
    private EditText PasswordEt;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    private String login_url = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/login2.php";
    private Session session;

    /**
     * Constructs the current page, assigning all View Vars and calling all methods needed
     * to display data to the user
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(getApplicationContext());

        if(session.isLoggedIn()){
            Intent i = new Intent(getApplicationContext(), ActivityFeed.class);
            startActivity(i);
            finish();
        }

        UsernameEt = (EditText)findViewById(R.id.usernameText);
        PasswordEt = (EditText)findViewById(R.id.passwordText);

        PasswordEt.setOnKeyListener(this);

        ConstraintLayout constraintLayout1 = (ConstraintLayout) findViewById(R.id.constraintLayout1);

        Button login = findViewById(R.id.login);

        constraintLayout1.setOnClickListener(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                username = UsernameEt.getText().toString();
                password = PasswordEt.getText().toString();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }

    /**
     * Sends the user to the Register page when they use the corresponding button.
     * @param view
     */

    public void goToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    /**
     * Displays Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Takes the inputted information for the user and uses that to attempt to log them in
     * to the system. If the user is successful, they are navigated to the ActivityFeed
     * page. Otherwise, an error is displayed.
     */

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(Constants.USERNAME, username);
            request.put(Constants.PASSWORD, password);
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
                            if (response.getInt(Constants.STATUS) == 0) {
                                session.loginUser(username, response.getInt("userId"), response.getString("bio"), response.getString("avatar"),
                                        response.getInt("followers"), response.getInt("type"));
                                Intent i = new Intent(getApplicationContext(), ActivityFeed.class);
                                startActivity(i);
                                finish();
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
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if(Constants.NULL.equals(username)){
            UsernameEt.setError("Username cannot be empty");
            UsernameEt.requestFocus();
            return false;
        }
        if(Constants.NULL.equals(password)){
            PasswordEt.setError("Password cannot be empty");
            PasswordEt.requestFocus();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }
}
