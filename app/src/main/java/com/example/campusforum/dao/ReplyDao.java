package com.example.campusforum.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.campusforum.database.DatabaseContract;
import com.example.campusforum.database.DatabaseHelper;
import com.example.campusforum.model.Reply;

import java.util.ArrayList;
import java.util.List;

public class ReplyDao {

    private static final String TAG = "ReplyDao";
    private static final String REPLY_ID_ALIAS = "reply_id";
    private static final String AUTHOR_NAME_ALIAS = "author_name";

    private final DatabaseHelper databaseHelper;

    public ReplyDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public long createReply(Reply reply) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Replies.COLUMN_TOPIC_ID, reply.getTopicId());
        values.put(DatabaseContract.Replies.COLUMN_AUTHOR_ID, reply.getAuthorId());
        values.put(DatabaseContract.Replies.COLUMN_CONTENT, reply.getContent());
        values.put(DatabaseContract.Replies.COLUMN_IS_DELETED, reply.isDeleted() ? 1 : 0);

        try {
            return db.insertOrThrow(DatabaseContract.Replies.TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to create reply", e);
            return -1;
        }
    }

    public int getActiveReplyCountByAuthor(long authorId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + DatabaseContract.Replies.TABLE_NAME +
                " WHERE " + DatabaseContract.Replies.COLUMN_AUTHOR_ID + " = ?" +
                " AND " + DatabaseContract.Replies.COLUMN_IS_DELETED + " = 0";
        String[] args = {String.valueOf(authorId)};

        try (Cursor cursor = db.rawQuery(sql, args)) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to count replies by author", e);
        }

        return 0;
    }

    public List<Reply> getActiveRepliesByTopic(long topicId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Reply> replies = new ArrayList<>();

        String sql = "SELECT r." + DatabaseContract.Replies._ID + " AS " + REPLY_ID_ALIAS +
                ", r." + DatabaseContract.Replies.COLUMN_TOPIC_ID +
                ", r." + DatabaseContract.Replies.COLUMN_AUTHOR_ID +
                ", u." + DatabaseContract.Users.COLUMN_USERNAME + " AS " + AUTHOR_NAME_ALIAS +
                ", r." + DatabaseContract.Replies.COLUMN_CONTENT +
                ", r." + DatabaseContract.Replies.COLUMN_CREATED_AT +
                ", r." + DatabaseContract.Replies.COLUMN_UPDATED_AT +
                ", r." + DatabaseContract.Replies.COLUMN_IS_DELETED +
                " FROM " + DatabaseContract.Replies.TABLE_NAME + " r" +
                " INNER JOIN " + DatabaseContract.Users.TABLE_NAME + " u ON r." +
                DatabaseContract.Replies.COLUMN_AUTHOR_ID + " = u." + DatabaseContract.Users._ID +
                " WHERE r." + DatabaseContract.Replies.COLUMN_TOPIC_ID + " = ?" +
                " AND r." + DatabaseContract.Replies.COLUMN_IS_DELETED + " = 0" +
                " ORDER BY r." + DatabaseContract.Replies.COLUMN_CREATED_AT + " ASC";
        String[] args = {String.valueOf(topicId)};

        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                replies.add(cursorToReply(cursor));
            }
        } catch (SQLiteException e) {
            Log.w(TAG, "Unable to get replies", e);
        }

        return replies;
    }

    private Reply cursorToReply(Cursor cursor) {
        Reply reply = new Reply();
        reply.setId(cursor.getLong(cursor.getColumnIndexOrThrow(REPLY_ID_ALIAS)));
        reply.setTopicId(cursor.getLong(cursor.getColumnIndexOrThrow(
                DatabaseContract.Replies.COLUMN_TOPIC_ID)));
        reply.setAuthorId(cursor.getLong(cursor.getColumnIndexOrThrow(
                DatabaseContract.Replies.COLUMN_AUTHOR_ID)));
        reply.setAuthorName(cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR_NAME_ALIAS)));
        reply.setContent(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Replies.COLUMN_CONTENT)));
        reply.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Replies.COLUMN_CREATED_AT)));
        reply.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.Replies.COLUMN_UPDATED_AT)));
        reply.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(
                DatabaseContract.Replies.COLUMN_IS_DELETED)) == 1);
        return reply;
    }
}
