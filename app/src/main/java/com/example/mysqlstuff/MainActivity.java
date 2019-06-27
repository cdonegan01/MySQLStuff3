package com.example.mysqlstuff;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    EditText UsernameEt, PasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        Backgroundworker backgroundworker = new Backgroundworker(this);
        backgroundworker.execute(type, username, password);

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
}
