package com.example.mysqlstuff;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mysqlstuff.objects.Constants;
import com.example.mysqlstuff.objects.User;
import com.example.mysqlstuff.objects.otherUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class Session {
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public Session(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     */
    public void loginUser(final String username, final int id, final String bio, final String avatar, final int followers, final int type) {
        mEditor.putString(Constants.USERNAME, username);
        mEditor.putInt(Constants.USERID, id);
        mEditor.putString(Constants.BIO, bio);
        mEditor.putString(Constants.AVATAR, avatar);
        mEditor.putInt(Constants.FOLLOWERS, followers);
        mEditor.putInt(Constants.TYPE, type);
        Date date = new Date();
        //Set user session for next 7 days
        long millis = date.getTime() + (24 * 60 * 60 * 1000);
        mEditor.putLong(Constants.EXPIRATION, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {

        long millis = mPreferences.getLong(Constants.EXPIRATION, 0);

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
        user.setUsername(mPreferences.getString(Constants.USERNAME, Constants.NULL));
        user.setUserId(mPreferences.getInt(Constants.USERID, 0));
        user.setBio(mPreferences.getString(Constants.BIO, Constants.NULL));
        user.setProfilePic_url(mPreferences.getString(Constants.AVATAR, Constants.NULL));
        user.setFollowers(mPreferences.getInt(Constants.FOLLOWERS,0));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(Constants.EXPIRATION, 0)));

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
