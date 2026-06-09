package com.example.campusforum.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.campusforum.database.DatabaseContract;

public class SessionManager {

    private static final String PREF_NAME = "CampusForumSession";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void createSession(long userId, String username, String email, String role) {
        preferences.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_USERNAME, username)
                .putString(KEY_EMAIL, email)
                .putString(KEY_ROLE, role)
                .apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public long getUserId() {
        return preferences.getLong(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, null);
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, null);
    }

    public String getRole() {
        return preferences.getString(KEY_ROLE, null);
    }

    public boolean isAdmin() {
        String role = getRole();
        return DatabaseContract.Users.ROLE_ADMIN.equals(role);
    }

    public void clearSession() {
        preferences.edit().clear().apply();
    }
}
