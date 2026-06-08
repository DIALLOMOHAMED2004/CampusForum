package com.example.campusforum.ui.topic;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.adapter.ReplyAdapter;
import com.example.campusforum.dao.ReplyDao;
import com.example.campusforum.dao.TopicDao;
import com.example.campusforum.model.Reply;
import com.example.campusforum.model.Topic;
import com.example.campusforum.utils.DateUtils;
import com.example.campusforum.utils.SessionManager;

import java.util.List;
import java.util.Locale;

public class TopicDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TOPIC_ID = "com.example.campusforum.EXTRA_TOPIC_ID";
    private static final int MIN_REPLY_LENGTH = 2;

    private TextView categoryText;
    private TextView titleText;
    private TextView contentText;
    private TextView authorText;
    private TextView createdAtText;
    private TextView replyCountText;
    private RecyclerView repliesRecyclerView;
    private View repliesEmptyState;
    private EditText replyInput;
    private TopicDao topicDao;
    private ReplyDao replyDao;
    private ReplyAdapter replyAdapter;
    private SessionManager sessionManager;
    private long topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        topicDao = new TopicDao(this);
        replyDao = new ReplyDao(this);
        sessionManager = new SessionManager(this);
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
        repliesRecyclerView = findViewById(R.id.topic_detail_replies_recycler_view);
        repliesEmptyState = findViewById(R.id.topic_detail_replies_empty_state);
        replyInput = findViewById(R.id.topic_detail_reply_input);

        replyAdapter = new ReplyAdapter();
        repliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        repliesRecyclerView.setNestedScrollingEnabled(false);
        repliesRecyclerView.setAdapter(replyAdapter);
    }

    private void setupActions() {
        findViewById(R.id.topic_detail_back_button).setOnClickListener(view -> finish());
        findViewById(R.id.topic_detail_send_reply_button).setOnClickListener(view -> sendReply());
    }

    private void loadTopic() {
        topicId = getIntent().getLongExtra(EXTRA_TOPIC_ID, -1);
        if (topicId <= 0) {
            showMissingTopicAndClose();
            return;
        }

        Topic topic = topicDao.getTopicById(topicId);
        if (topic == null) {
            showMissingTopicAndClose();
            return;
        }

        bindTopic(topic);
        loadReplies();
    }

    private void bindTopic(Topic topic) {
        categoryText.setText(topic.getCategoryName());
        titleText.setText(topic.getTitle());
        contentText.setText(topic.getContent());
        authorText.setText(getString(R.string.cf_author_format, topic.getAuthorName()));
        createdAtText.setText(getString(
                R.string.cf_topic_detail_created_at_format,
                DateUtils.formatRelativeDate(topic.getCreatedAt())));
        updateReplyCount(topic.getReplyCount());
    }

    private void loadReplies() {
        List<Reply> replies = replyDao.getActiveRepliesByTopic(topicId);
        replyAdapter.updateReplies(replies);
        updateReplyCount(replies.size());

        boolean hasReplies = !replies.isEmpty();
        repliesRecyclerView.setVisibility(hasReplies ? View.VISIBLE : View.GONE);
        repliesEmptyState.setVisibility(hasReplies ? View.GONE : View.VISIBLE);
    }

    private void sendReply() {
        replyInput.setError(null);
        String content = replyInput.getText().toString().trim();
        if (content.length() < MIN_REPLY_LENGTH) {
            replyInput.setError(getString(R.string.cf_topic_detail_reply_error_content));
            return;
        }

        long userId = sessionManager.getUserId();
        if (userId <= 0) {
            Toast.makeText(this, R.string.cf_create_topic_error_session, Toast.LENGTH_SHORT).show();
            return;
        }

        Reply reply = new Reply();
        reply.setTopicId(topicId);
        reply.setAuthorId(userId);
        reply.setContent(content);
        reply.setDeleted(false);

        long replyId = replyDao.createReply(reply);
        if (replyId > 0) {
            replyInput.setText("");
            loadReplies();
            Toast.makeText(this, R.string.cf_topic_detail_reply_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.cf_topic_detail_reply_error_publish, Toast.LENGTH_SHORT).show();
        }
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
