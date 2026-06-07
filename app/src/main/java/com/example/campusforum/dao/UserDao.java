package com.example.campusforum.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.campusforum.database.DatabaseContract;
import com.example.campusforum.database.DatabaseHelper;
import com.example.campusforum.model.User;

public class UserDao {

    private static final String TAG = "UserDao";

    private final DatabaseHelper databaseHelper;

    public UserDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public long createUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        try {
            return db.insertOrThrow(DatabaseContract.Users.TABLE_NAME, null, toContentValues(user));
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to create user", e);
            return -1;
        }
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = {DatabaseContract.Users._ID};
        String selection = DatabaseContract.Users.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        try (Cursor cursor = db.query(
                DatabaseContract.Users.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null,
                "1")) {
            return cursor.moveToFirst();
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to check email existence", e);
            return false;
        }
    }

    public User getUserByEmail(String email) {
        String selection = DatabaseContract.Users.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        return getSingleUser(selection, selectionArgs);
    }

    public User getUserById(long id) {
        String selection = DatabaseContract.Users._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return getSingleUser(selection, selectionArgs);
    }

    public String getSecurityQuestionByEmail(String email) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = {DatabaseContract.Users.COLUMN_SECURITY_QUESTION};
        String selection = DatabaseContract.Users.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        try (Cursor cursor = db.query(
                DatabaseContract.Users.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null,
                "1")) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseContract.Users.COLUMN_SECURITY_QUESTION));
            }
            return null;
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to get security question", e);
            return null;
        }
    }

    public boolean updatePassword(long userId, String passwordHash) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Users.COLUMN_PASSWORD_HASH, passwordHash);
        return updateUser(userId, values);
    }

    public boolean updateBio(long userId, String bio) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Users.COLUMN_BIO, bio);
        return updateUser(userId, values);
    }

    private User getSingleUser(String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        try (Cursor cursor = db.query(
                DatabaseContract.Users.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                "1")) {
            if (cursor.moveToFirst()) {
                return cursorToUser(cursor);
            }
            return null;
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to get user", e);
            return null;
        }
    }

    private boolean updateUser(long userId, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String whereClause = DatabaseContract.Users._ID + " = ?";
        String[] whereArgs = {String.valueOf(userId)};

        try {
            return db.update(DatabaseContract.Users.TABLE_NAME, values, whereClause, whereArgs) > 0;
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to update user", e);
            return false;
        }
    }

    private ContentValues toContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Users.COLUMN_USERNAME, user.getUsername());
        values.put(DatabaseContract.Users.COLUMN_EMAIL, user.getEmail());
        values.put(DatabaseContract.Users.COLUMN_PASSWORD_HASH, user.getPasswordHash());
        values.put(DatabaseContract.Users.COLUMN_SECURITY_QUESTION, user.getSecurityQuestion());
        values.put(DatabaseContract.Users.COLUMN_SECURITY_ANSWER_HASH, user.getSecurityAnswerHash());
        values.put(DatabaseContract.Users.COLUMN_ROLE, user.getRole());
        values.put(DatabaseContract.Users.COLUMN_BIO, user.getBio());
        values.put(DatabaseContract.Users.COLUMN_IS_ACTIVE, user.isActive() ? 1 : 0);
        return values;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Users._ID)));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Users.COLUMN_USERNAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Users.COLUMN_EMAIL)));
        user.setPasswordHash(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Users.COLUMN_PASSWORD_HASH)));
        user.setSecurityQuestion(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Users.COLUMN_SECURITY_QUESTION)));
        user.setSecurityAnswerHash(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Users.COLUMN_SECURITY_ANSWER_HASH)));
        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Users.COLUMN_ROLE)));
        user.setBio(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Users.COLUMN_BIO)));
        user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Users.COLUMN_CREATED_AT)));
        user.setActive(cursor.getInt(cursor.getColumnIndexOrThrow(
                DatabaseContract.Users.COLUMN_IS_ACTIVE)) == 1);
        return user;
    }
}
