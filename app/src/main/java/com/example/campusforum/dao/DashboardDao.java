package com.example.campusforum.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.campusforum.database.DatabaseContract;
import com.example.campusforum.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DashboardDao {

    private static final String TAG = "DashboardDao";
    private static final String TOPIC_ID_ALIAS = "topic_id";
    private static final String AUTHOR_NAME_ALIAS = "author_name";
    private static final String CATEGORY_NAME_ALIAS = "category_name";

    private final DatabaseHelper databaseHelper;

    public DashboardDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public int getActiveTopicCount() {
        return countRows(
                DatabaseContract.Topics.TABLE_NAME,
                DatabaseContract.Topics.COLUMN_IS_DELETED + " = ?",
                new String[]{"0"});
    }

    public int getActiveReplyCount() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + DatabaseContract.Replies.TABLE_NAME + " r" +
                " INNER JOIN " + DatabaseContract.Topics.TABLE_NAME + " t ON r." +
                DatabaseContract.Replies.COLUMN_TOPIC_ID + " = t." + DatabaseContract.Topics._ID +
                " WHERE r." + DatabaseContract.Replies.COLUMN_IS_DELETED + " = 0" +
                " AND t." + DatabaseContract.Topics.COLUMN_IS_DELETED + " = 0";

        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to count active replies", e);
        }

        return 0;
    }

    public int getActiveCategoryCount() {
        return countRows(
                DatabaseContract.Categories.TABLE_NAME,
                DatabaseContract.Categories.COLUMN_IS_ACTIVE + " = ?",
                new String[]{"1"});
    }

    public int getActiveMemberCount() {
        return countRows(
                DatabaseContract.Users.TABLE_NAME,
                DatabaseContract.Users.COLUMN_IS_ACTIVE + " = ?",
                new String[]{"1"});
    }

    public List<RecentTopic> getRecentTopics(int limit) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<RecentTopic> topics = new ArrayList<>();

        String sql = "SELECT t." + DatabaseContract.Topics._ID + " AS " + TOPIC_ID_ALIAS +
                ", t." + DatabaseContract.Topics.COLUMN_TITLE +
                ", t." + DatabaseContract.Topics.COLUMN_CREATED_AT +
                ", u." + DatabaseContract.Users.COLUMN_USERNAME + " AS " + AUTHOR_NAME_ALIAS +
                ", c." + DatabaseContract.Categories.COLUMN_NAME + " AS " + CATEGORY_NAME_ALIAS +
                " FROM " + DatabaseContract.Topics.TABLE_NAME + " t" +
                " INNER JOIN " + DatabaseContract.Users.TABLE_NAME + " u ON t." +
                DatabaseContract.Topics.COLUMN_AUTHOR_ID + " = u." + DatabaseContract.Users._ID +
                " INNER JOIN " + DatabaseContract.Categories.TABLE_NAME + " c ON t." +
                DatabaseContract.Topics.COLUMN_CATEGORY_ID + " = c." + DatabaseContract.Categories._ID +
                " WHERE t." + DatabaseContract.Topics.COLUMN_IS_DELETED + " = 0" +
                " ORDER BY t." + DatabaseContract.Topics.COLUMN_CREATED_AT + " DESC" +
                " LIMIT ?";
        String[] args = {String.valueOf(limit)};

        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                topics.add(cursorToRecentTopic(cursor));
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to get recent topics", e);
        }

        return topics;
    }

    private int countRows(String tableName, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + selection;

        try (Cursor cursor = db.rawQuery(sql, selectionArgs)) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to count rows in " + tableName, e);
        }

        return 0;
    }

    private RecentTopic cursorToRecentTopic(Cursor cursor) {
        return new RecentTopic(
                cursor.getLong(cursor.getColumnIndexOrThrow(TOPIC_ID_ALIAS)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Topics.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR_NAME_ALIAS)),
                cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_NAME_ALIAS)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Topics.COLUMN_CREATED_AT)));
    }

    public static final class RecentTopic {
        private final long id;
        private final String title;
        private final String authorName;
        private final String categoryName;
        private final String createdAt;

        public RecentTopic(long id, String title, String authorName, String categoryName, String createdAt) {
            this.id = id;
            this.title = title;
            this.authorName = authorName;
            this.categoryName = categoryName;
            this.createdAt = createdAt;
        }

        public long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthorName() {
            return authorName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
