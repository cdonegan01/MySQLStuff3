package com.example.mysqlstuff;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, password, email;
    RadioGroup pronoun;

    EditText usernameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        String placeholderUsername = getIntent().getStringExtra("EX_USER");
        String placeholderPassword = getIntent().getStringExtra("EX_PASS");

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        editTextUsername.setText(placeholderUsername);
        editTextPassword.setText(placeholderPassword);

        usernameET = findViewById(R.id.editTextUsername);
        passwordET = findViewById(R.id.editTextPassword);

        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        email = findViewById(R.id.editTextEmail);
        pronoun = findViewById(R.id.radioGender);

        ConstraintLayout constraintLayout2 = (ConstraintLayout) findViewById(R.id.constraintlayout2);

        constraintLayout2.setOnClickListener(this);

    }

    public void OnReg (View view) {
        String str_username = username.getText().toString();
        String str_password = password.getText().toString();
        String str_email = email.getText().toString();
        String type = "register";
        Backgroundworker backgroundworker = new Backgroundworker(this);
        backgroundworker.execute(type, str_username, str_password, str_email);

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
