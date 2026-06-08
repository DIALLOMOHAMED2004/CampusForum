package com.example.campusforum.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

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
}
