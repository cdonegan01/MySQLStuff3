package com.example.mysqlstuff;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mysqlstuff.objects.User;
import com.example.mysqlstuff.objects.otherUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class Session {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_ID = "userId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BIO = "bio";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_FOLLOWERS = "followers";
    private static final String KEY_TYPE = "type";
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
    public void loginUser(final String username, final int id, final String email, final String bio, final String avatar, final int followers, final int type) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putInt(KEY_ID, id);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_BIO, bio);
        mEditor.putString(KEY_AVATAR, avatar);
        mEditor.putInt(KEY_FOLLOWERS, followers);
        mEditor.putInt(KEY_TYPE, type);
        Date date = new Date();
        //Set user session for next 7 days
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
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setUserId(mPreferences.getInt(KEY_ID, 0));
        user.setEmail(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        user.setBio(mPreferences.getString(KEY_BIO, KEY_EMPTY));
        user.setProfilePic_url(mPreferences.getString(KEY_AVATAR, KEY_EMPTY));
        user.setFollowers(mPreferences.getInt(KEY_FOLLOWERS,0));
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
