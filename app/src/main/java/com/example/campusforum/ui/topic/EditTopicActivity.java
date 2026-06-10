package com.example.campusforum.ui.topic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusforum.R;
import com.example.campusforum.model.Topic;
import com.example.campusforum.repository.TopicRepository;

public class EditTopicActivity extends AppCompatActivity {

    private EditText titleInput;
    private TextView titleCounterText;
    private EditText contentInput;
    private Button saveButton;

    private TopicRepository topicRepository;
    private Topic topic;
    private long topicId;
    private boolean titleTouched;
    private boolean contentTouched;
    private boolean formSubmitted;
    private boolean saving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);

        topicRepository = new TopicRepository(this);
        topicId = getIntent().getLongExtra(TopicDetailActivity.EXTRA_TOPIC_ID, -1);
        if (topicId <= 0) {
            showMissingTopicAndClose();
            return;
        }

        topic = topicRepository.getTopicById(topicId);
        if (topic == null) {
            showMissingTopicAndClose();
            return;
        }

        if (!topicRepository.canEditTopic(topic)) {
            Toast.makeText(this, R.string.cf_topic_detail_error_forbidden, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindViews();
        setupActions();
        bindTopic();
        renderValidation(false);
    }

    private void bindViews() {
        titleInput = findViewById(R.id.edit_topic_title_input);
        titleCounterText = findViewById(R.id.edit_topic_title_counter);
        contentInput = findViewById(R.id.edit_topic_content_input);
        saveButton = findViewById(R.id.edit_topic_save_button);
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

        saveButton.setOnClickListener(view -> saveTopic());
        findViewById(R.id.edit_topic_cancel_button).setOnClickListener(view -> finish());
    }

    private void bindTopic() {
        titleInput.setText(topic.getTitle());
        contentInput.setText(topic.getContent());
    }

    private void saveTopic() {
        if (saving) {
            return;
        }

        formSubmitted = true;
        TopicRepository.TopicValidationResult validationResult = renderValidation(true);
        if (!validationResult.isValid()) {
            return;
        }

        saving = true;
        saveButton.setEnabled(false);
        TopicRepository.TopicActionResult result = topicRepository.updateTopic(
                topicId,
                getNormalizedTitle(),
                getNormalizedContent());
        saving = false;

        Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        if (result.isSuccess()) {
            finish();
        } else {
            renderValidation(true);
        }
    }

    private TopicRepository.TopicValidationResult renderValidation(boolean showAllErrors) {
        updateTitleCounter();

        TopicRepository.TopicValidationResult validationResult = topicRepository.validateDraft(
                getNormalizedTitle(),
                getNormalizedContent(),
                topic.getCategoryId());

        boolean showTitleError = showAllErrors || formSubmitted || titleTouched;
        boolean showContentError = showAllErrors || formSubmitted || contentTouched;

        titleInput.setError(showTitleError
                ? topicRepository.getTitleErrorMessage(validationResult.getTitleError())
                : null);
        contentInput.setError(showContentError
                ? topicRepository.getContentErrorMessage(validationResult.getContentError())
                : null);

        saveButton.setEnabled(validationResult.isValid() && !saving);
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

    private void showMissingTopicAndClose() {
        Toast.makeText(this, R.string.cf_topic_detail_error_missing, Toast.LENGTH_SHORT).show();
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
