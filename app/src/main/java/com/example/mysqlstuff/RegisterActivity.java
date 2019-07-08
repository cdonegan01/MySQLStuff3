package com.example.mysqlstuff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";

    private EditText etusername, etpassword, etconfirmPassword, etemail;
    private String username, password, confirmPassword, email;

    private ProgressDialog pDialog;
    private String register_url = "http://cdonegan01.lampt.eeecs.qub.ac.uk/projectstuff/register2.php";
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        String placeholderUsername = getIntent().getStringExtra("EX_USER");
        String placeholderPassword = getIntent().getStringExtra("EX_PASS");

        etusername = findViewById(R.id.editTextUsername);
        etpassword = findViewById(R.id.editTextPassword);

        etusername.setText(placeholderUsername);
        etpassword.setText(placeholderPassword);

        etconfirmPassword = findViewById(R.id.editTextConfirmPassword);
        etemail = findViewById(R.id.editTextEmail);

        ConstraintLayout constraintLayout2 = (ConstraintLayout) findViewById(R.id.constraintlayout2);

        constraintLayout2.setOnClickListener(this);

        Button register = findViewById(R.id.registerButton);

    }

    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), ActivityFeed.class);
        startActivity(i);
        finish();

    }

    public void OnReg (View view) {
        username = etusername.getText().toString().toLowerCase().trim();
        password = etpassword.getText().toString().trim();
        confirmPassword = etconfirmPassword.getText().toString().trim();
        email = etemail.getText().toString().trim();
        if (validateInputs()) {
            registerUser();
        }
    }

    private void registerUser() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, username);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_EMAIL, email);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                session.loginUser(username,email);
                                loadDashboard();

                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if username is already existsing
                                etusername.setError("Username already taken!");
                                etpassword.requestFocus();

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

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(username)) {
            etusername.setError("Username cannot be empty");
            etusername.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            etpassword.setError("Password cannot be empty");
            etpassword.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(confirmPassword)) {
            etconfirmPassword.setError("Please confirm your password.");
            etconfirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etconfirmPassword.setError("Password and Confirm Password does not match");
            etconfirmPassword.requestFocus();
            return false;
        }

        return true;
    }


    public void onClick(View view) {
        if (view.getId() == R.id.constraintLayout1) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void sendMessage (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
