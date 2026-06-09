package com.example.campusforum.ui.topic;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.adapter.ReplyAdapter;
import com.example.campusforum.model.Reply;
import com.example.campusforum.model.Topic;
import com.example.campusforum.repository.ReplyRepository;
import com.example.campusforum.repository.TopicRepository;
import com.example.campusforum.utils.DateUtils;

import java.util.List;
import java.util.Locale;

public class TopicDetailActivity extends AppCompatActivity implements ReplyAdapter.OnReplyActionListener {

    public static final String EXTRA_TOPIC_ID = "com.example.campusforum.EXTRA_TOPIC_ID";

    private TextView categoryText;
    private TextView titleText;
    private TextView contentText;
    private TextView authorText;
    private TextView createdAtText;
    private TextView replyCountText;
    private View topicActionsContainer;
    private RecyclerView repliesRecyclerView;
    private View repliesEmptyState;
    private EditText replyInput;
    private Button sendReplyButton;
    private TopicRepository topicRepository;
    private ReplyRepository replyRepository;
    private ReplyAdapter replyAdapter;
    private Topic currentTopic;
    private long topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        topicRepository = new TopicRepository(this);
        replyRepository = new ReplyRepository(this);
        bindViews();
        setupActions();
        loadTopic();
    }

    private void bindViews() {
        categoryText = findViewById(R.id.topic_detail_category);
        titleText = findViewById(R.id.topic_detail_title);
        contentText = findViewById(R.id.topic_detail_content);
        authorText = findViewById(R.id.topic_detail_author);
        createdAtText = findViewById(R.id.topic_detail_created_at);
        replyCountText = findViewById(R.id.topic_detail_reply_count);
        topicActionsContainer = findViewById(R.id.topic_detail_actions);
        repliesRecyclerView = findViewById(R.id.topic_detail_replies_recycler_view);
        repliesEmptyState = findViewById(R.id.topic_detail_replies_empty_state);
        replyInput = findViewById(R.id.topic_detail_reply_input);
        sendReplyButton = findViewById(R.id.topic_detail_send_reply_button);

        replyAdapter = new ReplyAdapter(this);
        repliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        repliesRecyclerView.setNestedScrollingEnabled(false);
        repliesRecyclerView.setAdapter(replyAdapter);
    }

    private void setupActions() {
        findViewById(R.id.topic_detail_back_button).setOnClickListener(view -> finish());
        findViewById(R.id.topic_detail_edit_button).setOnClickListener(view -> showEditTopicDialog());
        findViewById(R.id.topic_detail_delete_button).setOnClickListener(view -> confirmDeleteTopic());
        sendReplyButton.setOnClickListener(view -> sendReply());
    }

    private void loadTopic() {
        topicId = getIntent().getLongExtra(EXTRA_TOPIC_ID, -1);
        if (topicId <= 0) {
            showMissingTopicAndClose();
            return;
        }

        Topic topic = topicRepository.getTopicById(topicId);
        if (topic == null) {
            showMissingTopicAndClose();
            return;
        }

        bindTopic(topic);
        loadReplies();
    }

    private void bindTopic(Topic topic) {
        currentTopic = topic;
        categoryText.setText(topic.getCategoryName());
        titleText.setText(topic.getTitle());
        contentText.setText(topic.getContent());
        authorText.setText(getString(R.string.cf_author_format, topic.getAuthorName()));
        createdAtText.setText(getString(
                R.string.cf_topic_detail_created_at_format,
                DateUtils.formatRelativeDate(topic.getCreatedAt())));
        updateReplyCount(topic.getReplyCount());
        topicActionsContainer.setVisibility(topicRepository.canManageTopic(topic)
                ? View.VISIBLE
                : View.GONE);
    }

    private void loadReplies() {
        List<Reply> replies = replyRepository.getActiveRepliesByTopic(topicId);
        replyAdapter.updatePermissions(
                replyRepository.getCurrentUserId(),
                replyRepository.isCurrentUserAdmin());
        replyAdapter.updateReplies(replies);
        updateReplyCount(replies.size());

        boolean hasReplies = !replies.isEmpty();
        repliesRecyclerView.setVisibility(hasReplies ? View.VISIBLE : View.GONE);
        repliesEmptyState.setVisibility(hasReplies ? View.GONE : View.VISIBLE);
    }

    private void sendReply() {
        replyInput.setError(null);
        ReplyRepository.ReplyActionResult result = replyRepository.createReply(
                topicId,
                replyInput.getText().toString());
        if (result.isSuccess()) {
            replyInput.setText("");
            loadReplies();
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            replyInput.setError(result.getMessage());
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditReply(Reply reply) {
        showEditReplyDialog(reply);
    }

    @Override
    public void onDeleteReply(Reply reply) {
        confirmDeleteReply(reply);
    }

    private void showEditTopicDialog() {
        if (currentTopic == null) {
            showMissingTopicAndClose();
            return;
        }

        LinearLayout form = createDialogForm();
        EditText titleInput = createDialogInput(currentTopic.getTitle(), false);
        EditText contentInput = createDialogInput(currentTopic.getContent(), true);
        form.addView(titleInput);
        form.addView(contentInput);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.cf_topic_detail_topic_edit_title)
                .setView(form)
                .setNegativeButton(R.string.cf_action_cancel, null)
                .setPositiveButton(R.string.cf_action_save, null)
                .create();

        dialog.setOnShowListener(dialogInterface ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    TopicRepository.TopicValidationResult validationResult =
                            topicRepository.validateDraft(
                                    titleInput.getText().toString(),
                                    contentInput.getText().toString(),
                                    currentTopic.getCategoryId());
                    titleInput.setError(topicRepository.getTitleErrorMessage(
                            validationResult.getTitleError()));
                    contentInput.setError(topicRepository.getContentErrorMessage(
                            validationResult.getContentError()));
                    if (!validationResult.isValid()) {
                        return;
                    }

                    TopicRepository.TopicActionResult result = topicRepository.updateTopic(
                            topicId,
                            titleInput.getText().toString(),
                            contentInput.getText().toString());
                    Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.isSuccess()) {
                        dialog.dismiss();
                        loadTopic();
                    }
                }));
        dialog.show();
    }

    private void confirmDeleteTopic() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.cf_topic_detail_topic_delete_title)
                .setMessage(R.string.cf_topic_detail_topic_delete_confirm)
                .setNegativeButton(R.string.cf_action_cancel, null)
                .setPositiveButton(R.string.cf_action_delete, (dialog, which) -> deleteTopic())
                .show();
    }

    private void deleteTopic() {
        TopicRepository.TopicActionResult result = topicRepository.deleteTopic(topicId);
        Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        if (result.isSuccess()) {
            finish();
        }
    }

    private void showEditReplyDialog(Reply reply) {
        if (reply == null) {
            return;
        }

        LinearLayout form = createDialogForm();
        EditText contentInput = createDialogInput(reply.getContent(), true);
        form.addView(contentInput);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.cf_topic_detail_reply_edit_title)
                .setView(form)
                .setNegativeButton(R.string.cf_action_cancel, null)
                .setPositiveButton(R.string.cf_action_save, null)
                .create();

        dialog.setOnShowListener(dialogInterface ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    ReplyRepository.ReplyActionResult result = replyRepository.updateReply(
                            reply.getId(),
                            contentInput.getText().toString());
                    if (result.isSuccess()) {
                        Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadReplies();
                    } else {
                        contentInput.setError(result.getMessage());
                    }
                }));
        dialog.show();
    }

    private void confirmDeleteReply(Reply reply) {
        if (reply == null) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.cf_topic_detail_reply_delete_title)
                .setMessage(R.string.cf_topic_detail_reply_delete_confirm)
                .setNegativeButton(R.string.cf_action_cancel, null)
                .setPositiveButton(R.string.cf_action_delete,
                        (dialog, which) -> deleteReply(reply))
                .show();
    }

    private void deleteReply(Reply reply) {
        ReplyRepository.ReplyActionResult result = replyRepository.deleteReply(reply.getId());
        Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        if (result.isSuccess()) {
            loadReplies();
        }
    }

    private LinearLayout createDialogForm() {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        int padding = getResources().getDimensionPixelSize(R.dimen.cf_spacing_4);
        form.setPadding(padding, padding, padding, 0);
        return form;
    }

    private EditText createDialogInput(String value, boolean multiline) {
        EditText input = new EditText(this);
        input.setText(value);
        input.setSelectAllOnFocus(false);
        input.setInputType(multiline
                ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        if (multiline) {
            input.setMinLines(4);
            input.setGravity(android.view.Gravity.TOP | android.view.Gravity.START);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.cf_spacing_3));
        input.setLayoutParams(layoutParams);
        return input;
    }

    private void updateReplyCount(int replyCount) {
        String replyLabel = replyCount > 1
                ? getString(R.string.cf_topic_detail_replies_label)
                : getString(R.string.cf_topic_detail_reply_label);
        replyCountText.setText(String.format(Locale.getDefault(), "%d %s", replyCount, replyLabel));
    }

    private void showMissingTopicAndClose() {
        Toast.makeText(this, R.string.cf_topic_detail_error_missing, Toast.LENGTH_SHORT).show();
        finish();
    }
}
