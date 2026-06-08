package com.example.campusforum.model;

public class Category {

    private long id;
    private String name;
    private String description;
    private String createdAt;
    private boolean active = true;

    public Category() {
    }

    public Category(long id, String name, String description, String createdAt, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
