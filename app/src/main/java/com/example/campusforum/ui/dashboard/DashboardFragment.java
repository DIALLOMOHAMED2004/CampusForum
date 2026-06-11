package com.example.campusforum.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.adapter.DashboardRecentTopicAdapter;
import com.example.campusforum.repository.DashboardRepository;
import com.example.campusforum.ui.topic.TopicDetailActivity;

import java.util.List;

public class DashboardFragment extends Fragment {

    private static final long REFRESH_ENABLE_DELAY_MS = 500L;

    private TextView topicsCountText;
    private TextView repliesCountText;
    private TextView categoriesCountText;
    private TextView membersCountText;
    private View adminBadgeView;
    private View membersCardView;
    private RecyclerView recentActivitiesRecyclerView;
    private View recentActivitiesEmptyView;
    private ImageButton refreshButton;
    private DashboardRecentTopicAdapter recentTopicAdapter;
    private DashboardRepository dashboardRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dashboardRepository = new DashboardRepository(requireContext());
        bindViews(view);
        setupRecentActivities();
        setupRefresh();
        loadDashboard();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dashboardRepository != null) {
            loadDashboard();
        }
    }

    private void bindViews(View view) {
        topicsCountText = view.findViewById(R.id.dashboard_topics_count);
        repliesCountText = view.findViewById(R.id.dashboard_replies_count);
        categoriesCountText = view.findViewById(R.id.dashboard_categories_count);
        membersCountText = view.findViewById(R.id.dashboard_members_count);
        adminBadgeView = view.findViewById(R.id.dashboard_admin_badge);
        membersCardView = view.findViewById(R.id.dashboard_members_card);
        recentActivitiesRecyclerView = view.findViewById(R.id.dashboard_recent_activities_recycler_view);
        recentActivitiesEmptyView = view.findViewById(R.id.dashboard_recent_activities_empty_state);
        refreshButton = view.findViewById(R.id.dashboard_refresh_button);
    }

    private void setupRecentActivities() {
        recentTopicAdapter = new DashboardRecentTopicAdapter(this::openTopicDetail);
        recentActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recentActivitiesRecyclerView.setAdapter(recentTopicAdapter);
    }

    private void setupRefresh() {
        refreshButton.setOnClickListener(view -> {
            refreshButton.setEnabled(false);
            loadDashboard();
            refreshButton.postDelayed(
                    () -> refreshButton.setEnabled(true),
                    REFRESH_ENABLE_DELAY_MS);
        });
    }

    private void loadDashboard() {
        DashboardRepository.DashboardState dashboardState = dashboardRepository.getDashboardState();
        topicsCountText.setText(String.valueOf(dashboardState.getActiveTopicCount()));
        repliesCountText.setText(String.valueOf(dashboardState.getActiveReplyCount()));
        categoriesCountText.setText(String.valueOf(dashboardState.getActiveCategoryCount()));

        membersCardView.setVisibility(dashboardState.shouldShowMemberCount() ? View.VISIBLE : View.GONE);
        adminBadgeView.setVisibility(dashboardState.shouldShowMemberCount() ? View.VISIBLE : View.GONE);
        if (dashboardState.shouldShowMemberCount()) {
            membersCountText.setText(String.valueOf(dashboardState.getActiveMemberCount()));
        }

        List<DashboardRepository.RecentTopic> recentTopics = dashboardState.getRecentTopics();
        recentTopicAdapter.updateTopics(recentTopics);
        boolean hasRecentTopics = !recentTopics.isEmpty();
        recentActivitiesRecyclerView.setVisibility(hasRecentTopics ? View.VISIBLE : View.GONE);
        recentActivitiesEmptyView.setVisibility(hasRecentTopics ? View.GONE : View.VISIBLE);
    }

    private void openTopicDetail(DashboardRepository.RecentTopic topic) {
        if (topic == null || topic.getId() <= 0) {
            return;
        }
        Intent intent = new Intent(requireContext(), TopicDetailActivity.class);
        intent.putExtra(TopicDetailActivity.EXTRA_TOPIC_ID, topic.getId());
        startActivity(intent);
    }
}
