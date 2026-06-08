package com.example.campusforum.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campusforum.R;
import com.example.campusforum.dao.DashboardDao;
import com.example.campusforum.utils.DateUtils;

import java.util.List;

public class DashboardFragment extends Fragment {

    private static final int RECENT_TOPIC_LIMIT = 2;

    private TextView topicsCountText;
    private TextView repliesCountText;
    private TextView categoriesCountText;
    private TextView membersCountText;
    private View firstRecentActivityView;
    private View secondRecentActivityView;
    private TextView firstRecentActivityTitleText;
    private TextView firstRecentActivityMetaText;
    private TextView secondRecentActivityTitleText;
    private TextView secondRecentActivityMetaText;
    private DashboardDao dashboardDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dashboardDao = new DashboardDao(requireContext());
        bindViews(view);
        loadDashboard();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dashboardDao != null) {
            loadDashboard();
        }
    }

    private void bindViews(View view) {
        topicsCountText = view.findViewById(R.id.dashboard_topics_count);
        repliesCountText = view.findViewById(R.id.dashboard_replies_count);
        categoriesCountText = view.findViewById(R.id.dashboard_categories_count);
        membersCountText = view.findViewById(R.id.dashboard_members_count);
        firstRecentActivityView = view.findViewById(R.id.dashboard_recent_activity_first);
        secondRecentActivityView = view.findViewById(R.id.dashboard_recent_activity_second);
        firstRecentActivityTitleText = view.findViewById(R.id.dashboard_recent_activity_first_title);
        firstRecentActivityMetaText = view.findViewById(R.id.dashboard_recent_activity_first_meta);
        secondRecentActivityTitleText = view.findViewById(R.id.dashboard_recent_activity_second_title);
        secondRecentActivityMetaText = view.findViewById(R.id.dashboard_recent_activity_second_meta);
    }

    private void loadDashboard() {
        topicsCountText.setText(String.valueOf(dashboardDao.getActiveTopicCount()));
        repliesCountText.setText(String.valueOf(dashboardDao.getActiveReplyCount()));
        categoriesCountText.setText(String.valueOf(dashboardDao.getActiveCategoryCount()));
        membersCountText.setText(String.valueOf(dashboardDao.getActiveMemberCount()));
        bindRecentTopics(dashboardDao.getRecentTopics(RECENT_TOPIC_LIMIT));
    }

    private void bindRecentTopics(List<DashboardDao.RecentTopic> recentTopics) {
        if (recentTopics.isEmpty()) {
            firstRecentActivityView.setVisibility(View.VISIBLE);
            secondRecentActivityView.setVisibility(View.GONE);
            firstRecentActivityTitleText.setText(R.string.cf_dashboard_no_recent_activity);
            firstRecentActivityMetaText.setText(R.string.cf_dashboard_no_recent_activity_message);
            return;
        }

        bindRecentTopic(recentTopics.get(0), firstRecentActivityView,
                firstRecentActivityTitleText, firstRecentActivityMetaText);

        if (recentTopics.size() > 1) {
            bindRecentTopic(recentTopics.get(1), secondRecentActivityView,
                    secondRecentActivityTitleText, secondRecentActivityMetaText);
        } else {
            secondRecentActivityView.setVisibility(View.GONE);
        }
    }

    private void bindRecentTopic(DashboardDao.RecentTopic topic, View container,
            TextView titleText, TextView metaText) {
        container.setVisibility(View.VISIBLE);
        titleText.setText(topic.getTitle());
        metaText.setText(getString(
                R.string.cf_dashboard_recent_topic_meta,
                getString(R.string.cf_author_format, topic.getAuthorName()),
                topic.getCategoryName(),
                DateUtils.formatRelativeDate(topic.getCreatedAt())));
    }
}
