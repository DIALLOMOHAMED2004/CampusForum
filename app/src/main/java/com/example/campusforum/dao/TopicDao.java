package com.example.campusforum.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.campusforum.database.DatabaseContract;
import com.example.campusforum.database.DatabaseHelper;
import com.example.campusforum.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicDao {

    private static final String TAG = "TopicDao";

    private static final String TOPIC_ID_ALIAS = "topic_id";
    private static final String AUTHOR_NAME_ALIAS = "author_name";
    private static final String CATEGORY_NAME_ALIAS = "category_name";
    private static final String REPLY_COUNT_ALIAS = "reply_count";

    private final DatabaseHelper databaseHelper;

    public TopicDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public long createTopic(Topic topic) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Topics.COLUMN_TITLE, topic.getTitle());
        values.put(DatabaseContract.Topics.COLUMN_CONTENT, topic.getContent());
        values.put(DatabaseContract.Topics.COLUMN_AUTHOR_ID, topic.getAuthorId());
        values.put(DatabaseContract.Topics.COLUMN_CATEGORY_ID, topic.getCategoryId());
        values.put(DatabaseContract.Topics.COLUMN_IS_DELETED, topic.isDeleted() ? 1 : 0);

        try {
            return db.insertOrThrow(DatabaseContract.Topics.TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to create topic", e);
            return -1;
        }
    }

    public List<Topic> getActiveTopics() {
        return getTopics(null, null);
    }

    public List<Topic> getRecentActiveTopicsByAuthor(long authorId, int limit) {
        if (authorId <= 0 || limit <= 0) {
            return new ArrayList<>();
        }

        String selection = "t." + DatabaseContract.Topics.COLUMN_AUTHOR_ID + " = ?";
        String[] selectionArgs = {String.valueOf(authorId)};
        return getTopics(selection, selectionArgs, limit);
    }

    public Topic getTopicById(long topicId) {
        String selection = "t." + DatabaseContract.Topics._ID + " = ?";
        String[] selectionArgs = {String.valueOf(topicId)};
        List<Topic> topics = getTopics(selection, selectionArgs);
        return topics.isEmpty() ? null : topics.get(0);
    }

    public boolean updateTopic(long topicId, String title, String content, long categoryId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String sql = "UPDATE " + DatabaseContract.Topics.TABLE_NAME +
                " SET " + DatabaseContract.Topics.COLUMN_TITLE + " = ?, " +
                DatabaseContract.Topics.COLUMN_CONTENT + " = ?, " +
                DatabaseContract.Topics.COLUMN_CATEGORY_ID + " = ?, " +
                DatabaseContract.Topics.COLUMN_UPDATED_AT + " = CURRENT_TIMESTAMP" +
                " WHERE " + DatabaseContract.Topics._ID + " = ?" +
                " AND " + DatabaseContract.Topics.COLUMN_IS_DELETED + " = 0";
        Object[] args = {title, content, categoryId, topicId};

        try {
            db.execSQL(sql, args);
            return getTopicById(topicId) != null;
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to update topic", e);
            return false;
        }
    }

    public boolean softDeleteTopic(long topicId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues topicValues = new ContentValues();
        topicValues.put(DatabaseContract.Topics.COLUMN_IS_DELETED, 1);

        String topicWhereClause = DatabaseContract.Topics._ID + " = ? AND " +
                DatabaseContract.Topics.COLUMN_IS_DELETED + " = 0";
        String[] topicWhereArgs = {String.valueOf(topicId)};

        ContentValues replyValues = new ContentValues();
        replyValues.put(DatabaseContract.Replies.COLUMN_IS_DELETED, 1);

        String replyWhereClause = DatabaseContract.Replies.COLUMN_TOPIC_ID + " = ? AND " +
                DatabaseContract.Replies.COLUMN_IS_DELETED + " = 0";
        String[] replyWhereArgs = {String.valueOf(topicId)};

        boolean transactionStarted = false;
        try {
            db.beginTransaction();
            transactionStarted = true;
            int deletedTopics = db.update(
                    DatabaseContract.Topics.TABLE_NAME,
                    topicValues,
                    topicWhereClause,
                    topicWhereArgs);
            if (deletedTopics <= 0) {
                return false;
            }

            db.update(
                    DatabaseContract.Replies.TABLE_NAME,
                    replyValues,
                    replyWhereClause,
                    replyWhereArgs);
            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to delete topic", e);
            return false;
        } finally {
            if (transactionStarted) {
                db.endTransaction();
            }
        }
    }

    public List<Topic> searchActiveTopics(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveTopics();
        }

        String selection = "(t." + DatabaseContract.Topics.COLUMN_TITLE + " LIKE ? OR t." +
                DatabaseContract.Topics.COLUMN_CONTENT + " LIKE ?)";
        String likeKeyword = "%" + keyword.trim() + "%";
        String[] selectionArgs = {likeKeyword, likeKeyword};
        return getTopics(selection, selectionArgs);
    }

    public List<Topic> getActiveTopicsByCategory(long categoryId) {
        String selection = "t." + DatabaseContract.Topics.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};
        return getTopics(selection, selectionArgs);
    }

    public int getActiveTopicCountByAuthor(long authorId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + DatabaseContract.Topics.TABLE_NAME +
                " WHERE " + DatabaseContract.Topics.COLUMN_AUTHOR_ID + " = ?" +
                " AND " + DatabaseContract.Topics.COLUMN_IS_DELETED + " = 0";
        String[] args = {String.valueOf(authorId)};

        try (Cursor cursor = db.rawQuery(sql, args)) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to count topics by author", e);
        }

        return 0;
    }

    public List<Topic> searchActiveTopicsByCategory(String keyword, long categoryId) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveTopicsByCategory(categoryId);
        }

        String selection = "t." + DatabaseContract.Topics.COLUMN_CATEGORY_ID + " = ? AND " +
                "(t." + DatabaseContract.Topics.COLUMN_TITLE + " LIKE ? OR t." +
                DatabaseContract.Topics.COLUMN_CONTENT + " LIKE ?)";
        String likeKeyword = "%" + keyword.trim() + "%";
        String[] selectionArgs = {String.valueOf(categoryId), likeKeyword, likeKeyword};
        return getTopics(selection, selectionArgs);
    }

    private List<Topic> getTopics(String extraSelection, String[] extraSelectionArgs) {
        return getTopics(extraSelection, extraSelectionArgs, -1);
    }

    private List<Topic> getTopics(String extraSelection, String[] extraSelectionArgs, int limit) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Topic> topics = new ArrayList<>();

        String sql = buildTopicsQuery(extraSelection, limit);
        String[] args = extraSelectionArgs == null ? new String[0] : extraSelectionArgs;

        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                topics.add(cursorToTopic(cursor));
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to get topics", e);
        }

        return topics;
    }

    private String buildTopicsQuery(String extraSelection) {
        return buildTopicsQuery(extraSelection, -1);
    }

    private String buildTopicsQuery(String extraSelection, int limit) {
        String topicsTable = DatabaseContract.Topics.TABLE_NAME;
        String usersTable = DatabaseContract.Users.TABLE_NAME;
        String categoriesTable = DatabaseContract.Categories.TABLE_NAME;
        String repliesTable = DatabaseContract.Replies.TABLE_NAME;

        StringBuilder query = new StringBuilder();
        query.append("SELECT ")
                .append("t.").append(DatabaseContract.Topics._ID).append(" AS ").append(TOPIC_ID_ALIAS)
                .append(", t.").append(DatabaseContract.Topics.COLUMN_TITLE)
                .append(", t.").append(DatabaseContract.Topics.COLUMN_CONTENT)
                .append(", t.").append(DatabaseContract.Topics.COLUMN_AUTHOR_ID)
                .append(", u.").append(DatabaseContract.Users.COLUMN_USERNAME).append(" AS ")
                .append(AUTHOR_NAME_ALIAS)
                .append(", t.").append(DatabaseContract.Topics.COLUMN_CATEGORY_ID)
                .append(", c.").append(DatabaseContract.Categories.COLUMN_NAME).append(" AS ")
                .append(CATEGORY_NAME_ALIAS)
                .append(", t.").append(DatabaseContract.Topics.COLUMN_CREATED_AT)
                .append(", t.").append(DatabaseContract.Topics.COLUMN_UPDATED_AT)
                .append(", t.").append(DatabaseContract.Topics.COLUMN_IS_DELETED)
                .append(", COUNT(r.").append(DatabaseContract.Replies._ID).append(") AS ")
                .append(REPLY_COUNT_ALIAS)
                .append(" FROM ").append(topicsTable).append(" t")
                .append(" INNER JOIN ").append(usersTable).append(" u ON t.")
                .append(DatabaseContract.Topics.COLUMN_AUTHOR_ID).append(" = u.")
                .append(DatabaseContract.Users._ID)
                .append(" INNER JOIN ").append(categoriesTable).append(" c ON t.")
                .append(DatabaseContract.Topics.COLUMN_CATEGORY_ID).append(" = c.")
                .append(DatabaseContract.Categories._ID)
                .append(" LEFT JOIN ").append(repliesTable).append(" r ON r.")
                .append(DatabaseContract.Replies.COLUMN_TOPIC_ID).append(" = t.")
                .append(DatabaseContract.Topics._ID)
                .append(" AND r.").append(DatabaseContract.Replies.COLUMN_IS_DELETED).append(" = 0")
                .append(" WHERE t.").append(DatabaseContract.Topics.COLUMN_IS_DELETED).append(" = 0");

        if (extraSelection != null && !extraSelection.trim().isEmpty()) {
            query.append(" AND ").append(extraSelection);
        }

        query.append(" GROUP BY t.").append(DatabaseContract.Topics._ID)
                .append(" ORDER BY t.").append(DatabaseContract.Topics.COLUMN_CREATED_AT).append(" DESC");

        if (limit > 0) {
            query.append(" LIMIT ").append(limit);
        }

        return query.toString();
    }

    private Topic cursorToTopic(Cursor cursor) {
        Topic topic = new Topic();
        topic.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TOPIC_ID_ALIAS)));
        topic.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Topics.COLUMN_TITLE)));
        topic.setContent(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Topics.COLUMN_CONTENT)));
        topic.setAuthorId(cursor.getLong(cursor.getColumnIndexOrThrow(
                DatabaseContract.Topics.COLUMN_AUTHOR_ID)));
        topic.setAuthorName(cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR_NAME_ALIAS)));
        topic.setCategoryId(cursor.getLong(cursor.getColumnIndexOrThrow(
                DatabaseContract.Topics.COLUMN_CATEGORY_ID)));
        topic.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_NAME_ALIAS)));
        topic.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Topics.COLUMN_CREATED_AT)));
        topic.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Topics.COLUMN_UPDATED_AT)));
        topic.setReplyCount(cursor.getInt(cursor.getColumnIndexOrThrow(REPLY_COUNT_ALIAS)));
        topic.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(
                DatabaseContract.Topics.COLUMN_IS_DELETED)) == 1);
        return topic;
    }
}
