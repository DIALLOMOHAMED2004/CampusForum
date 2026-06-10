package com.example.campusforum.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.adapter.TopicAdapter;
import com.example.campusforum.dao.ReplyDao;
import com.example.campusforum.dao.TopicDao;
import com.example.campusforum.dao.UserDao;
import com.example.campusforum.model.Topic;
import com.example.campusforum.model.User;
import com.example.campusforum.repository.AuthRepository;
import com.example.campusforum.ui.auth.LoginActivity;
import com.example.campusforum.ui.topic.TopicDetailActivity;
import com.example.campusforum.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private static final int BIO_MAX_LENGTH = 200;
    private static final int RECENT_TOPICS_LIMIT = 5;

    private TextView avatarInitialsText;
    private TextView usernameText;
    private TextView emailText;
    private TextView roleText;
    private TextView bioText;
    private TextView editBioButton;
    private TextView topicsCountText;
    private TextView repliesCountText;
    private View recentTopicsTitle;
    private RecyclerView recentTopicsRecyclerView;
    private View recentTopicsEmptyState;
    private SessionManager sessionManager;
    private UserDao userDao;
    private TopicDao topicDao;
    private ReplyDao replyDao;
    private TopicAdapter recentTopicAdapter;
    private long currentUserId = -1;
    private String currentBio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        userDao = new UserDao(requireContext());
        topicDao = new TopicDao(requireContext());
        replyDao = new ReplyDao(requireContext());

        bindViews(view);
        setupRecentTopics();
        editBioButton.setOnClickListener(v -> showEditBioDialog());
        view.findViewById(R.id.profile_logout_button).setOnClickListener(v -> confirmLogout());
        loadProfile();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager != null) {
            loadProfile();
        }
    }

    private void bindViews(View view) {
        avatarInitialsText = view.findViewById(R.id.profile_avatar_initials_text);
        usernameText = view.findViewById(R.id.profile_username_text);
        emailText = view.findViewById(R.id.profile_email_text);
        roleText = view.findViewById(R.id.profile_role_text);
        bioText = view.findViewById(R.id.profile_bio_text);
        editBioButton = view.findViewById(R.id.profile_edit_bio_button);
        topicsCountText = view.findViewById(R.id.profile_topics_count_text);
        repliesCountText = view.findViewById(R.id.profile_replies_count_text);
        recentTopicsTitle = view.findViewById(R.id.profile_recent_topics_title);
        recentTopicsRecyclerView = view.findViewById(R.id.profile_recent_topics_recycler_view);
        recentTopicsEmptyState = view.findViewById(R.id.profile_recent_topics_empty_state);
    }

    private void setupRecentTopics() {
        recentTopicAdapter = new TopicAdapter(new ArrayList<>(), this::openTopicDetail);
        recentTopicsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recentTopicsRecyclerView.setNestedScrollingEnabled(false);
        recentTopicsRecyclerView.setAdapter(recentTopicAdapter);
    }

    private void loadProfile() {
        long userId = sessionManager.getUserId();
        if (userId <= 0) {
            logout();
            return;
        }

        User user = userDao.getUserById(userId);
        if (user == null || !user.isActive()) {
            logout();
            return;
        }

        currentUserId = userId;
        currentBio = user.getBio();
        usernameText.setText(user.getUsername());
        emailText.setText(user.getEmail());
        roleText.setText(user.getRole());
        avatarInitialsText.setText(getInitials(user.getUsername()));
        bioText.setText(isBlank(currentBio) ? getString(R.string.cf_profile_empty_bio) : currentBio);
        topicsCountText.setText(String.valueOf(topicDao.getActiveTopicCountByAuthor(userId)));
        repliesCountText.setText(String.valueOf(replyDao.getActiveReplyCountByAuthor(userId)));
        updateRecentTopics(topicDao.getRecentActiveTopicsByAuthor(userId, RECENT_TOPICS_LIMIT));
    }

    private String getInitials(String username) {
        if (isBlank(username)) {
            return getString(R.string.cf_brand_initials);
        }

        String[] parts = username.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0));
            }
            if (initials.length() == 2) {
                break;
            }
        }

        return initials.toString().toUpperCase(Locale.getDefault());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void updateRecentTopics(List<Topic> recentTopics) {
        boolean hasRecentTopics = recentTopics != null && !recentTopics.isEmpty();
        recentTopicsTitle.setVisibility(View.VISIBLE);
        recentTopicsRecyclerView.setVisibility(hasRecentTopics ? View.VISIBLE : View.GONE);
        recentTopicsEmptyState.setVisibility(hasRecentTopics ? View.GONE : View.VISIBLE);
        recentTopicAdapter.updateTopics(recentTopics);
    }

    private void showEditBioDialog() {
        EditText bioInput = new EditText(requireContext());
        bioInput.setHint(R.string.cf_profile_bio_hint);
        bioInput.setMinLines(3);
        bioInput.setMaxLines(5);
        bioInput.setSingleLine(false);
        bioInput.setText(isBlank(currentBio) ? "" : currentBio);
        bioInput.setSelection(bioInput.getText().length());

        FrameLayout inputContainer = new FrameLayout(requireContext());
        int horizontalPadding = getResources().getDimensionPixelSize(R.dimen.cf_spacing_4);
        inputContainer.setPadding(horizontalPadding, 0, horizontalPadding, 0);
        inputContainer.addView(bioInput, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.cf_profile_edit_bio_title)
                .setView(inputContainer)
                .setNegativeButton(R.string.cf_action_cancel, null)
                .setPositiveButton(R.string.cf_action_save, null)
                .create();
        dialog.setOnShowListener(dialogInterface ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view ->
                        saveBio(dialog, bioInput)));
        dialog.show();
    }

    private void saveBio(AlertDialog dialog, EditText bioInput) {
        String bio = bioInput.getText().toString().trim();
        if (bio.length() > BIO_MAX_LENGTH) {
            bioInput.setError(getString(R.string.cf_profile_bio_error_length));
            return;
        }

        if (currentUserId <= 0) {
            Toast.makeText(requireContext(), R.string.cf_profile_bio_update_error, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = userDao.updateBio(currentUserId, bio);
        if (!updated) {
            Toast.makeText(requireContext(), R.string.cf_profile_bio_update_error, Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.dismiss();
        Toast.makeText(requireContext(), R.string.cf_profile_bio_update_success, Toast.LENGTH_SHORT).show();
        loadProfile();
    }

    private void openTopicDetail(Topic topic) {
        if (topic == null || topic.getId() <= 0) {
            return;
        }

        Intent intent = new Intent(requireContext(), TopicDetailActivity.class);
        intent.putExtra(TopicDetailActivity.EXTRA_TOPIC_ID, topic.getId());
        startActivity(intent);
    }

    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.cf_profile_logout)
                .setMessage(R.string.cf_profile_logout_confirm_message)
                .setNegativeButton(R.string.cf_action_cancel, null)
                .setPositiveButton(R.string.cf_profile_logout, (dialog, which) -> logout())
                .show();
    }

    private void logout() {
        new AuthRepository(requireContext()).logout();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
