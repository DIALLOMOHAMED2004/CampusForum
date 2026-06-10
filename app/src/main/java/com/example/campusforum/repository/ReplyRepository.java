package com.example.campusforum.repository;

import android.content.Context;

import com.example.campusforum.R;
import com.example.campusforum.dao.ReplyDao;
import com.example.campusforum.dao.TopicDao;
import com.example.campusforum.model.Reply;
import com.example.campusforum.model.Topic;
import com.example.campusforum.utils.SessionManager;

import java.util.List;

public class ReplyRepository {

    public static final int MIN_REPLY_LENGTH = 2;

    private final Context context;
    private final ReplyDao replyDao;
    private final TopicDao topicDao;
    private final SessionManager sessionManager;

    public ReplyRepository(Context context) {
        this.context = context.getApplicationContext();
        replyDao = new ReplyDao(context);
        topicDao = new TopicDao(context);
        sessionManager = new SessionManager(context);
    }

    public List<Reply> getActiveRepliesByTopic(long topicId) {
        return replyDao.getActiveRepliesByTopic(topicId);
    }

    public ReplyActionResult createReply(long topicId, String content) {
        String normalizedContent = normalize(content);
        String validationError = getContentValidationError(normalizedContent);
        if (validationError != null) {
            return ReplyActionResult.failure(validationError);
        }

        long userId = sessionManager.getUserId();
        if (userId <= 0) {
            return ReplyActionResult.failure(context.getString(R.string.cf_create_topic_error_session));
        }

        Topic topic = topicDao.getTopicById(topicId);
        if (topic == null) {
            return ReplyActionResult.failure(context.getString(R.string.cf_topic_detail_error_missing));
        }

        Reply reply = new Reply();
        reply.setTopicId(topicId);
        reply.setAuthorId(userId);
        reply.setContent(normalizedContent);
        reply.setDeleted(false);

        long replyId = replyDao.createReply(reply);
        if (replyId <= 0) {
            return ReplyActionResult.failure(context.getString(R.string.cf_topic_detail_reply_error_publish));
        }

        return ReplyActionResult.success(context.getString(R.string.cf_topic_detail_reply_success));
    }

    public ReplyActionResult updateReply(long replyId, String content) {
        String normalizedContent = normalize(content);
        String validationError = getContentValidationError(normalizedContent);
        if (validationError != null) {
            return ReplyActionResult.failure(validationError);
        }

        Reply reply = replyDao.getReplyById(replyId);
        if (reply == null) {
            return ReplyActionResult.failure(context.getString(R.string.cf_topic_detail_reply_error_missing));
        }

        if (!canEditReply(reply)) {
            return ReplyActionResult.failure(context.getString(R.string.cf_topic_detail_error_forbidden));
        }

        boolean updated = replyDao.updateReplyContent(replyId, normalizedContent);
        if (!updated) {
            return ReplyActionResult.failure(context.getString(R.string.cf_topic_detail_reply_error_update));
        }

        return ReplyActionResult.success(context.getString(R.string.cf_topic_detail_reply_success_update));
    }

    public ReplyActionResult deleteReply(long replyId) {
        Reply reply = replyDao.getReplyById(replyId);
        if (reply == null) {
            return ReplyActionResult.failure(context.getString(R.string.cf_topic_detail_reply_error_missing));
        }

        if (!canDeleteReply(reply)) {
            return ReplyActionResult.failure(context.getString(R.string.cf_topic_detail_error_forbidden));
        }

        boolean deleted = replyDao.softDeleteReply(replyId);
        if (!deleted) {
            return ReplyActionResult.failure(context.getString(R.string.cf_topic_detail_reply_error_delete));
        }

        return ReplyActionResult.success(context.getString(R.string.cf_topic_detail_reply_success_delete));
    }

    public boolean canManageReply(Reply reply) {
        return canEditReply(reply) || canDeleteReply(reply);
    }

    public boolean canEditReply(Reply reply) {
        if (reply == null) {
            return false;
        }
        long userId = sessionManager.getUserId();
        return userId > 0 && reply.getAuthorId() == userId;
    }

    public boolean canDeleteReply(Reply reply) {
        if (reply == null) {
            return false;
        }
        long userId = sessionManager.getUserId();
        return userId > 0 && (reply.getAuthorId() == userId || sessionManager.isAdmin());
    }

    public long getCurrentUserId() {
        return sessionManager.getUserId();
    }

    public boolean isCurrentUserAdmin() {
        return sessionManager.isAdmin();
    }

    private String getContentValidationError(String content) {
        if (content.isEmpty() || content.length() < MIN_REPLY_LENGTH) {
            return context.getString(R.string.cf_topic_detail_reply_error_content);
        }
        return null;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public static final class ReplyActionResult {
        private final boolean success;
        private final String message;

        private ReplyActionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static ReplyActionResult success(String message) {
            return new ReplyActionResult(true, message);
        }

        public static ReplyActionResult failure(String message) {
            return new ReplyActionResult(false, message);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
