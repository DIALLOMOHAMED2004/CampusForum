package com.example.campusforum.ui.topic;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusforum.R;
import com.example.campusforum.dao.CategoryDao;
import com.example.campusforum.dao.TopicDao;
import com.example.campusforum.model.Category;
import com.example.campusforum.model.Topic;
import com.example.campusforum.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CreateTopicActivity extends AppCompatActivity {

    private static final int MIN_TITLE_LENGTH = 5;
    private static final int MIN_CONTENT_LENGTH = 10;

    private EditText titleInput;
    private EditText contentInput;
    private Spinner categorySpinner;
    private Button publishButton;
    private List<Category> categories = new ArrayList<>();
    private TopicDao topicDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

        topicDao = new TopicDao(this);
        sessionManager = new SessionManager(this);

        bindViews();
        setupActions();
        loadCategories();
    }

    private void bindViews() {
        titleInput = findViewById(R.id.create_topic_title_input);
        contentInput = findViewById(R.id.create_topic_content_input);
        categorySpinner = findViewById(R.id.create_topic_category_spinner);
        publishButton = findViewById(R.id.create_topic_publish_button);
    }

    private void setupActions() {
        publishButton.setOnClickListener(view -> publishTopic());
        findViewById(R.id.create_topic_cancel_button).setOnClickListener(view -> finish());
    }

    private void loadCategories() {
        categories = new CategoryDao(this).getActiveCategories();

        List<String> categoryNames = new ArrayList<>();
        categoryNames.add(getString(R.string.cf_create_topic_category_placeholder));
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void publishTopic() {
        clearErrors();

        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();
        int selectedCategoryPosition = categorySpinner.getSelectedItemPosition();

        if (!validateInputs(title, content, selectedCategoryPosition)) {
            return;
        }

        long userId = sessionManager.getUserId();
        if (userId <= 0) {
            Toast.makeText(this, R.string.cf_create_topic_error_session, Toast.LENGTH_SHORT).show();
            return;
        }

        Category selectedCategory = categories.get(selectedCategoryPosition - 1);
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setAuthorId(userId);
        topic.setCategoryId(selectedCategory.getId());
        topic.setDeleted(false);

        publishButton.setEnabled(false);
        long topicId = topicDao.createTopic(topic);
        publishButton.setEnabled(true);

        if (topicId > 0) {
            Toast.makeText(this, R.string.cf_create_topic_success, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.cf_create_topic_error_publish, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String title, String content, int selectedCategoryPosition) {
        boolean valid = true;

        if (title.length() < MIN_TITLE_LENGTH) {
            titleInput.setError(getString(R.string.cf_create_topic_error_title));
            valid = false;
        }
        if (content.length() < MIN_CONTENT_LENGTH) {
            contentInput.setError(getString(R.string.cf_create_topic_error_content));
            valid = false;
        }
        if (selectedCategoryPosition <= 0 || categories.isEmpty()) {
            Toast.makeText(this, R.string.cf_create_topic_error_category, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void clearErrors() {
        titleInput.setError(null);
        contentInput.setError(null);
    }
}
