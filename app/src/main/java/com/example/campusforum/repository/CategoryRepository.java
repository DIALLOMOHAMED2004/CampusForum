package com.example.campusforum.repository;

import android.content.Context;

import com.example.campusforum.dao.CategoryDao;
import com.example.campusforum.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private final CategoryDao categoryDao;

    public CategoryRepository(Context context) {
        categoryDao = new CategoryDao(context);
    }

    public List<Category> getActiveCategories() {
        List<Category> categories = categoryDao.getActiveCategories();
        return categories == null ? new ArrayList<>() : categories;
    }

    public boolean hasActiveCategories() {
        return !getActiveCategories().isEmpty();
    }
}
