package com.example.campusforum.repository;

import android.content.Context;

import com.example.campusforum.R;
import com.example.campusforum.dao.CategoryDao;
import com.example.campusforum.model.Category;
import com.example.campusforum.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private static final int MAX_CATEGORY_NAME_LENGTH = 40;

    private final Context context;
    private final CategoryDao categoryDao;
    private final SessionManager sessionManager;

    public CategoryRepository(Context context) {
        this.context = context.getApplicationContext();
        categoryDao = new CategoryDao(context);
        sessionManager = new SessionManager(context);
    }

    public List<Category> getActiveCategories() {
        List<Category> categories = categoryDao.getActiveCategories();
        return categories == null ? new ArrayList<>() : categories;
    }

    public boolean hasActiveCategories() {
        return !getActiveCategories().isEmpty();
    }

    public CategoryActionResult createCategory(String name, String description) {
        if (!sessionManager.isAdmin()) {
            return CategoryActionResult.failure(context.getString(R.string.cf_admin_error_access_required));
        }

        String normalizedName = normalize(name);
        String normalizedDescription = normalize(description);
        if (normalizedName.isEmpty()) {
            return CategoryActionResult.failure(context.getString(R.string.cf_admin_error_category_name_required));
        }
        if (normalizedName.length() > MAX_CATEGORY_NAME_LENGTH) {
            return CategoryActionResult.failure(context.getString(R.string.cf_admin_error_category_name_length));
        }
        if (categoryDao.categoryNameExists(normalizedName)) {
            return CategoryActionResult.failure(context.getString(R.string.cf_admin_error_category_duplicate));
        }

        long categoryId = categoryDao.createCategory(
                normalizedName,
                normalizedDescription.isEmpty() ? null : normalizedDescription);
        if (categoryId <= 0) {
            return CategoryActionResult.failure(context.getString(R.string.cf_admin_error_category_add));
        }

        return CategoryActionResult.success(context.getString(R.string.cf_admin_success_category_added));
    }

    public CategoryActionResult deleteCategory(long categoryId) {
        if (!sessionManager.isAdmin()) {
            return CategoryActionResult.failure(context.getString(R.string.cf_admin_error_access_required));
        }
        if (categoryId <= 0) {
            return CategoryActionResult.failure(context.getString(R.string.cf_admin_error_category_delete));
        }

        Category category = categoryDao.getCategoryById(categoryId);
        if (category == null || !category.isActive()) {
            return CategoryActionResult.failure(context.getString(R.string.cf_admin_error_category_delete));
        }

        boolean deleted = categoryDao.softDeleteCategory(categoryId);
        if (!deleted) {
            return CategoryActionResult.failure(context.getString(R.string.cf_admin_error_category_delete));
        }

        return CategoryActionResult.success(context.getString(R.string.cf_admin_success_category_deleted));
    }

    public boolean isCurrentUserAdmin() {
        return sessionManager.isAdmin();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public static final class CategoryActionResult {
        private final boolean success;
        private final String message;

        private CategoryActionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static CategoryActionResult success(String message) {
            return new CategoryActionResult(true, message);
        }

        public static CategoryActionResult failure(String message) {
            return new CategoryActionResult(false, message);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
