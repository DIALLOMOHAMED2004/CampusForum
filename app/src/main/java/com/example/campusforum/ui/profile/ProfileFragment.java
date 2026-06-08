package com.example.campusforum.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campusforum.R;
import com.example.campusforum.dao.ReplyDao;
import com.example.campusforum.dao.TopicDao;
import com.example.campusforum.dao.UserDao;
import com.example.campusforum.model.User;
import com.example.campusforum.repository.AuthRepository;
import com.example.campusforum.ui.auth.LoginActivity;
import com.example.campusforum.utils.SessionManager;

import java.util.Locale;

public class ProfileFragment extends Fragment {

    private TextView avatarInitialsText;
    private TextView usernameText;
    private TextView emailText;
    private TextView roleText;
    private TextView bioText;
    private TextView topicsCountText;
    private TextView repliesCountText;
    private View recentTopicsTitle;
    private View recentTopicOneCard;
    private View recentTopicTwoCard;
    private SessionManager sessionManager;
    private UserDao userDao;
    private TopicDao topicDao;
    private ReplyDao replyDao;

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
        topicsCountText = view.findViewById(R.id.profile_topics_count_text);
        repliesCountText = view.findViewById(R.id.profile_replies_count_text);
        recentTopicsTitle = view.findViewById(R.id.profile_recent_topics_title);
        recentTopicOneCard = view.findViewById(R.id.profile_recent_topic_one_card);
        recentTopicTwoCard = view.findViewById(R.id.profile_recent_topic_two_card);
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

        usernameText.setText(user.getUsername());
        emailText.setText(user.getEmail());
        roleText.setText(user.getRole());
        avatarInitialsText.setText(getInitials(user.getUsername()));
        bioText.setText(isBlank(user.getBio()) ? getString(R.string.cf_profile_empty_bio) : user.getBio());
        topicsCountText.setText(String.valueOf(topicDao.getActiveTopicCountByAuthor(userId)));
        repliesCountText.setText(String.valueOf(replyDao.getActiveReplyCountByAuthor(userId)));
        hideStaticRecentTopics();
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

    private void hideStaticRecentTopics() {
        recentTopicsTitle.setVisibility(View.GONE);
        recentTopicOneCard.setVisibility(View.GONE);
        recentTopicTwoCard.setVisibility(View.GONE);
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
