package com.example.campusforum.ui.topic;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusforum.R;
import com.example.campusforum.model.Category;
import com.example.campusforum.repository.CategoryRepository;
import com.example.campusforum.repository.TopicRepository;
import com.example.campusforum.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CreateTopicActivity extends AppCompatActivity {

    private static final long NO_CATEGORY_SELECTED = -1L;

    private EditText titleInput;
    private TextView titleCounterText;
    private Spinner categorySpinner;
    private TextView categoryErrorText;
    private EditText contentInput;
    private Button publishButton;

    private final List<Category> categories = new ArrayList<>();
    private TopicRepository topicRepository;
    private CategoryRepository categoryRepository;
    private SessionManager sessionManager;

    private boolean titleTouched;
    private boolean contentTouched;
    private boolean categoryTouched;
    private boolean formSubmitted;
    private boolean publishing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

        topicRepository = new TopicRepository(this);
        categoryRepository = new CategoryRepository(this);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn() || sessionManager.getUserId() <= 0) {
            Toast.makeText(this, R.string.cf_create_topic_error_session, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindViews();
        setupActions();
        loadCategories();
        renderValidation(false);
    }

    private void bindViews() {
        titleInput = findViewById(R.id.create_topic_title_input);
        titleCounterText = findViewById(R.id.create_topic_title_counter);
        categorySpinner = findViewById(R.id.create_topic_category_spinner);
        categoryErrorText = findViewById(R.id.create_topic_category_error);
        contentInput = findViewById(R.id.create_topic_content_input);
        publishButton = findViewById(R.id.create_topic_publish_button);
    }

    private void setupActions() {
        titleInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                titleTouched = true;
                renderValidation(false);
            }
        });

        contentInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                contentTouched = true;
                renderValidation(false);
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    categoryTouched = true;
                }
                renderValidation(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                renderValidation(false);
            }
        });

        publishButton.setOnClickListener(view -> publishTopic());
        findViewById(R.id.create_topic_cancel_button).setOnClickListener(view -> finish());
    }

    private void loadCategories() {
        categories.clear();
        categories.addAll(categoryRepository.getActiveCategories());

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
        categorySpinner.setEnabled(!categories.isEmpty());

        if (categories.isEmpty()) {
            categoryErrorText.setText(R.string.cf_create_topic_error_no_categories);
            categoryErrorText.setVisibility(View.VISIBLE);
        }
    }

    private void publishTopic() {
        if (publishing) {
            return;
        }

        formSubmitted = true;
        TopicRepository.TopicValidationResult validationResult = renderValidation(true);
        if (!validationResult.isValid()) {
            return;
        }

        publishing = true;
        publishButton.setEnabled(false);

        TopicRepository.TopicCreationResult creationResult = topicRepository.createTopic(
                getNormalizedTitle(),
                getNormalizedContent(),
                getSelectedCategoryId());

        publishing = false;

        Toast.makeText(this, creationResult.getMessage(), Toast.LENGTH_SHORT).show();
        if (creationResult.isSuccess()) {
            openTopicDetail(creationResult.getTopicId());
        } else {
            renderValidation(true);
        }
    }

    private TopicRepository.TopicValidationResult renderValidation(boolean showAllErrors) {
        updateTitleCounter();

        TopicRepository.TopicValidationResult validationResult = topicRepository.validateDraft(
                getNormalizedTitle(),
                getNormalizedContent(),
                getSelectedCategoryId());

        boolean showTitleError = showAllErrors || formSubmitted || titleTouched;
        boolean showContentError = showAllErrors || formSubmitted || contentTouched;
        boolean showCategoryError = showAllErrors || formSubmitted || categoryTouched;

        titleInput.setError(showTitleError
                ? topicRepository.getTitleErrorMessage(validationResult.getTitleError())
                : null);
        contentInput.setError(showContentError
                ? topicRepository.getContentErrorMessage(validationResult.getContentError())
                : null);

        if (categories.isEmpty()) {
            categoryErrorText.setText(R.string.cf_create_topic_error_no_categories);
            categoryErrorText.setVisibility(View.VISIBLE);
        } else {
            String categoryError = showCategoryError
                    ? topicRepository.getCategoryErrorMessage(validationResult.getCategoryError())
                    : null;
            categoryErrorText.setText(categoryError);
            categoryErrorText.setVisibility(categoryError == null ? View.GONE : View.VISIBLE);
        }

        publishButton.setEnabled(validationResult.isValid() && !publishing && topicRepository.hasLoggedInUser());
        return validationResult;
    }

    private void updateTitleCounter() {
        titleCounterText.setText(getString(
                R.string.cf_create_topic_title_counter,
                getNormalizedTitle().length()));
    }

    private String getNormalizedTitle() {
        return titleInput.getText().toString().trim();
    }

    private String getNormalizedContent() {
        return contentInput.getText().toString().trim();
    }

    private long getSelectedCategoryId() {
        int selectedPosition = categorySpinner.getSelectedItemPosition();
        if (selectedPosition <= 0 || selectedPosition > categories.size()) {
            return NO_CATEGORY_SELECTED;
        }
        return categories.get(selectedPosition - 1).getId();
    }

    private void openTopicDetail(long topicId) {
        Intent intent = new Intent(this, TopicDetailActivity.class);
        intent.putExtra(TopicDetailActivity.EXTRA_TOPIC_ID, topicId);
        startActivity(intent);
        finish();
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence text, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {
        }
    }
}
