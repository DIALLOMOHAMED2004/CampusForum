package com.example.campusforum.model;

public class Topic {

    private long id;
    private String title;
    private String content;
    private long authorId;
    private String authorName;
    private long categoryId;
    private String categoryName;
    private String createdAt;
    private String updatedAt;
    private int replyCount;
    private boolean deleted;

    public Topic() {
    }

    public Topic(String category, String title, String preview, String authorInfo,
            String relativeDate, int replyCount) {
        this.categoryName = category;
        this.title = title;
        this.content = preview;
        this.authorName = authorInfo;
        this.createdAt = relativeDate;
        this.replyCount = replyCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return categoryName;
    }

    public String getPreview() {
        return content;
    }

    public String getAuthorInfo() {
        return authorName;
    }

    public String getRelativeDate() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
