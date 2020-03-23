/*
 *
 *  * Created by Deepak Kaushik
 *  * Copyright (c) 2019. All right reserved.
 *  * Last Modified 15/9/19 10:13 PM
 *
 */

package com.board.flipr.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.board.flipr.Model.User;

public class SharedPrefHelper {

    public static final String TAG = "SharedPrefManager";
    private static final String SHARED_PREF_NAME = "flipr_board";
    public static final String KEY_TOKEN = "token";
    private static final String KEY_VISITED = "has_walk_through_visited";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PROFILE = "user_color";


    private Context ctx;

    private SharedPrefHelper(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefHelper getInstance(Context context) {
        return new SharedPrefHelper(context.getApplicationContext());
    }

    public boolean hasVisitedWalkThrough() {
        SharedPreferences preferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_VISITED, false);
    }

    public void setVisitedWalkThrough(Boolean visited) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_VISITED, visited);
        editor.apply();
    }



    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, null);
        editor.putString(KEY_TOKEN, null);
        editor.apply();

    }

    public String getUserName() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_USER_NAME, null);
        if (name != null && name.length() > 0) {
            return name;
        }
        return null;
    }

    public void setUserName(String name) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public void setUserData(User user){
        SharedPreferences preferences  = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putInt(KEY_USER_PROFILE, user.getColor());
        editor.apply();
    }

    public boolean isUserLoggedIn(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_USER_NAME, null);
        return name != null && name.length() > 0;
    }

    public User getUserData(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_USER_NAME, null);
        String email = sharedPreferences.getString(KEY_USER_EMAIL, null);
        String id = sharedPreferences.getString(KEY_USER_ID, null);
        int color = sharedPreferences.getInt(KEY_USER_PROFILE, -1);

        return new User(id, name, email, color);
    }


}