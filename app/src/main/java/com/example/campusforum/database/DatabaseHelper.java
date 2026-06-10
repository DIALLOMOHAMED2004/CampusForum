package com.example.campusforum.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.campusforum.utils.PasswordUtils;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DEFAULT_ADMIN_USERNAME = "Administrateur";
    private static final String DEFAULT_ADMIN_EMAIL = "admin@campusforum.local";
    private static final String DEFAULT_ADMIN_PASSWORD = "Admin123";
    private static final String DEFAULT_ADMIN_SECURITY_QUESTION = "Compte administrateur par défaut";
    private static final String DEFAULT_ADMIN_SECURITY_ANSWER = "admin";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Users.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Categories.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Topics.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Replies.SQL_CREATE_TABLE);

        createIndexes(db);
        insertDefaultCategories(db);
        insertDefaultAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Version 1 is the initial schema. Future migrations must be explicit and non-destructive.
    }

    private void createIndexes(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Users.SQL_CREATE_EMAIL_INDEX);
        db.execSQL(DatabaseContract.Users.SQL_CREATE_ROLE_INDEX);
        db.execSQL(DatabaseContract.Categories.SQL_CREATE_NAME_INDEX);
        db.execSQL(DatabaseContract.Topics.SQL_CREATE_AUTHOR_INDEX);
        db.execSQL(DatabaseContract.Topics.SQL_CREATE_CATEGORY_INDEX);
        db.execSQL(DatabaseContract.Topics.SQL_CREATE_DELETED_INDEX);
        db.execSQL(DatabaseContract.Topics.SQL_CREATE_CREATED_AT_INDEX);
        db.execSQL(DatabaseContract.Replies.SQL_CREATE_TOPIC_INDEX);
        db.execSQL(DatabaseContract.Replies.SQL_CREATE_AUTHOR_INDEX);
        db.execSQL(DatabaseContract.Replies.SQL_CREATE_DELETED_INDEX);
        db.execSQL(DatabaseContract.Replies.SQL_CREATE_CREATED_AT_INDEX);
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        for (String[] category : DatabaseContract.Categories.DEFAULT_CATEGORIES) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Categories.COLUMN_NAME, category[0]);
            values.put(DatabaseContract.Categories.COLUMN_DESCRIPTION, category[1]);
            values.put(DatabaseContract.Categories.COLUMN_IS_ACTIVE, 1);
            db.insert(DatabaseContract.Categories.TABLE_NAME, null, values);
        }
    }

    public void ensureDefaultAdmin() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            insertDefaultAdmin(db);
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to ensure default admin", e);
        }
    }

    private void insertDefaultAdmin(SQLiteDatabase db) {
        if (defaultAdminExists(db)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Users.COLUMN_USERNAME, DEFAULT_ADMIN_USERNAME);
        values.put(DatabaseContract.Users.COLUMN_EMAIL, DEFAULT_ADMIN_EMAIL);
        values.put(DatabaseContract.Users.COLUMN_PASSWORD_HASH,
                PasswordUtils.hashPassword(DEFAULT_ADMIN_PASSWORD));
        values.put(DatabaseContract.Users.COLUMN_SECURITY_QUESTION, DEFAULT_ADMIN_SECURITY_QUESTION);
        values.put(DatabaseContract.Users.COLUMN_SECURITY_ANSWER_HASH,
                PasswordUtils.hashSecurityAnswer(DEFAULT_ADMIN_SECURITY_ANSWER));
        values.put(DatabaseContract.Users.COLUMN_ROLE, DatabaseContract.Users.ROLE_ADMIN);
        values.put(DatabaseContract.Users.COLUMN_IS_ACTIVE, 1);
        db.insertWithOnConflict(
                DatabaseContract.Users.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE);
    }

    private boolean defaultAdminExists(SQLiteDatabase db) {
        String[] columns = {DatabaseContract.Users._ID};
        String selection = DatabaseContract.Users.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {DEFAULT_ADMIN_EMAIL};

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
        }
    }
}
