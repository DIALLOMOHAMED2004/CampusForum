package com.example.campusforum.repository;

import android.content.Context;

import com.example.campusforum.R;
import com.example.campusforum.dao.CategoryDao;
import com.example.campusforum.dao.TopicDao;
import com.example.campusforum.model.Category;
import com.example.campusforum.model.Topic;
import com.example.campusforum.utils.SessionManager;

public class TopicRepository {

    public static final int MIN_TITLE_LENGTH = 5;
    public static final int MIN_CONTENT_LENGTH = 10;

    private final Context context;
    private final TopicDao topicDao;
    private final CategoryDao categoryDao;
    private final SessionManager sessionManager;

    public TopicRepository(Context context) {
        this.context = context.getApplicationContext();
        topicDao = new TopicDao(context);
        categoryDao = new CategoryDao(context);
        sessionManager = new SessionManager(context);
    }

    public boolean hasLoggedInUser() {
        return sessionManager.getUserId() > 0;
    }

    public TopicValidationResult validateDraft(String title, String content, long categoryId) {
        String normalizedTitle = normalize(title);
        String normalizedContent = normalize(content);

        FieldError titleError = FieldError.NONE;
        FieldError contentError = FieldError.NONE;
        FieldError categoryError = FieldError.NONE;

        if (normalizedTitle.isEmpty()) {
            titleError = FieldError.REQUIRED;
        } else if (normalizedTitle.length() < MIN_TITLE_LENGTH) {
            titleError = FieldError.TOO_SHORT;
        }

        if (normalizedContent.isEmpty()) {
            contentError = FieldError.REQUIRED;
        } else if (normalizedContent.length() < MIN_CONTENT_LENGTH) {
            contentError = FieldError.TOO_SHORT;
        }

        if (categoryId <= 0) {
            categoryError = FieldError.REQUIRED;
        }

        return new TopicValidationResult(titleError, contentError, categoryError);
    }

    public TopicCreationResult createTopic(String title, String content, long categoryId) {
        TopicValidationResult validationResult = validateDraft(title, content, categoryId);
        if (!validationResult.isValid()) {
            return TopicCreationResult.failure(context.getString(R.string.cf_create_topic_error_invalid_form));
        }

        long authorId = sessionManager.getUserId();
        if (authorId <= 0) {
            return TopicCreationResult.failure(context.getString(R.string.cf_create_topic_error_session));
        }

        Category category = categoryDao.getCategoryById(categoryId);
        if (category == null || !category.isActive()) {
            return TopicCreationResult.failure(context.getString(R.string.cf_create_topic_error_category));
        }

        Topic topic = new Topic();
        topic.setTitle(normalize(title));
        topic.setContent(normalize(content));
        topic.setAuthorId(authorId);
        topic.setCategoryId(categoryId);
        topic.setDeleted(false);

        long topicId = topicDao.createTopic(topic);
        if (topicId <= 0) {
            return TopicCreationResult.failure(context.getString(R.string.cf_create_topic_error_publish));
        }

        return TopicCreationResult.success(
                context.getString(R.string.cf_create_topic_success),
                topicId);
    }

    public String getTitleErrorMessage(FieldError error) {
        if (error == FieldError.REQUIRED) {
            return context.getString(R.string.cf_create_topic_error_title_required);
        }
        if (error == FieldError.TOO_SHORT) {
            return context.getString(R.string.cf_create_topic_error_title);
        }
        return null;
    }

    public String getContentErrorMessage(FieldError error) {
        if (error == FieldError.REQUIRED) {
            return context.getString(R.string.cf_create_topic_error_content_required);
        }
        if (error == FieldError.TOO_SHORT) {
            return context.getString(R.string.cf_create_topic_error_content);
        }
        return null;
    }

    public String getCategoryErrorMessage(FieldError error) {
        if (error == FieldError.REQUIRED) {
            return context.getString(R.string.cf_create_topic_error_category);
        }
        return null;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public enum FieldError {
        NONE,
        REQUIRED,
        TOO_SHORT
    }

    public static final class TopicValidationResult {
        private final FieldError titleError;
        private final FieldError contentError;
        private final FieldError categoryError;

        private TopicValidationResult(FieldError titleError, FieldError contentError,
                FieldError categoryError) {
            this.titleError = titleError;
            this.contentError = contentError;
            this.categoryError = categoryError;
        }

        public boolean isValid() {
            return titleError == FieldError.NONE
                    && contentError == FieldError.NONE
                    && categoryError == FieldError.NONE;
        }

        public FieldError getTitleError() {
            return titleError;
        }

        public FieldError getContentError() {
            return contentError;
        }

        public FieldError getCategoryError() {
            return categoryError;
        }
    }

    public static final class TopicCreationResult {
        private final boolean success;
        private final String message;
        private final long topicId;

        private TopicCreationResult(boolean success, String message, long topicId) {
            this.success = success;
            this.message = message;
            this.topicId = topicId;
        }

        public static TopicCreationResult success(String message, long topicId) {
            return new TopicCreationResult(true, message, topicId);
        }

        public static TopicCreationResult failure(String message) {
            return new TopicCreationResult(false, message, -1);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public long getTopicId() {
            return topicId;
        }
    }
}
