package com.example.campusforum.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.campusforum.database.DatabaseContract;
import com.example.campusforum.database.DatabaseHelper;
import com.example.campusforum.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    private static final String TAG = "CategoryDao";

    private final DatabaseHelper databaseHelper;

    public CategoryDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public List<Category> getActiveCategories() {
        ensureDefaultCategories();
        return queryActiveCategories();
    }

    private List<Category> queryActiveCategories() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = DatabaseContract.Categories.COLUMN_IS_ACTIVE + " = ?";
        String[] selectionArgs = {"1"};
        String orderBy = DatabaseContract.Categories.COLUMN_NAME + " ASC";
        List<Category> categories = new ArrayList<>();

        try (Cursor cursor = db.query(
                DatabaseContract.Categories.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                orderBy)) {
            while (cursor.moveToNext()) {
                categories.add(cursorToCategory(cursor));
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to get active categories", e);
        }

        return categories;
    }

    private void ensureDefaultCategories() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        try {
            for (String[] category : DatabaseContract.Categories.DEFAULT_CATEGORIES) {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Categories.COLUMN_NAME, category[0]);
                values.put(DatabaseContract.Categories.COLUMN_DESCRIPTION, category[1]);
                values.put(DatabaseContract.Categories.COLUMN_IS_ACTIVE, 1);
                db.insertWithOnConflict(
                        DatabaseContract.Categories.TABLE_NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_IGNORE);
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to ensure default categories", e);
        }
    }

    public Category getCategoryById(long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = DatabaseContract.Categories._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        try (Cursor cursor = db.query(
                DatabaseContract.Categories.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                "1")) {
            if (cursor.moveToFirst()) {
                return cursorToCategory(cursor);
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to get category by id", e);
        }

        return null;
    }

    private Category cursorToCategory(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Categories._ID)));
        category.setName(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Categories.COLUMN_NAME)));
        category.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Categories.COLUMN_DESCRIPTION)));
        category.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Categories.COLUMN_CREATED_AT)));
        category.setActive(cursor.getInt(cursor.getColumnIndexOrThrow(
                DatabaseContract.Categories.COLUMN_IS_ACTIVE)) == 1);
        return category;
    }
}
