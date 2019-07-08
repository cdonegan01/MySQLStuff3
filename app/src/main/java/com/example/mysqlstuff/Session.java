package com.example.mysqlstuff;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mysqlstuff.objects.User;

import java.util.Date;

public class Session {


    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public Session(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     * @param email
     */
    public void loginUser(String username, String email) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_EMAIL, email);
        Date date = new Date();

        long millis = date.getTime() + (1 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return true;
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setEmail(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }

}
