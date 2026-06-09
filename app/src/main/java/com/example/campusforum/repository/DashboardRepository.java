package com.example.campusforum.repository;

import android.content.Context;

import com.example.campusforum.dao.DashboardDao;
import com.example.campusforum.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class DashboardRepository {

    private static final int RECENT_TOPIC_LIMIT = 5;

    private final DashboardDao dashboardDao;
    private final SessionManager sessionManager;

    public DashboardRepository(Context context) {
        dashboardDao = new DashboardDao(context);
        sessionManager = new SessionManager(context);
    }

    public DashboardState getDashboardState() {
        boolean admin = sessionManager.isAdmin();
        int activeMemberCount = admin ? dashboardDao.getActiveMemberCount() : 0;
        List<RecentTopic> recentTopics = mapRecentTopics(
                dashboardDao.getRecentTopics(RECENT_TOPIC_LIMIT));

        return new DashboardState(
                dashboardDao.getActiveTopicCount(),
                dashboardDao.getActiveReplyCount(),
                dashboardDao.getActiveCategoryCount(),
                activeMemberCount,
                admin,
                recentTopics);
    }

    private List<RecentTopic> mapRecentTopics(List<DashboardDao.RecentTopic> daoTopics) {
        List<RecentTopic> topics = new ArrayList<>();
        if (daoTopics == null) {
            return topics;
        }

        for (DashboardDao.RecentTopic topic : daoTopics) {
            topics.add(new RecentTopic(
                    topic.getId(),
                    topic.getTitle(),
                    topic.getAuthorName(),
                    topic.getCategoryName(),
                    topic.getCreatedAt()));
        }
        return topics;
    }

    public static final class DashboardState {
        private final int activeTopicCount;
        private final int activeReplyCount;
        private final int activeCategoryCount;
        private final int activeMemberCount;
        private final boolean showMemberCount;
        private final List<RecentTopic> recentTopics;

        private DashboardState(int activeTopicCount, int activeReplyCount, int activeCategoryCount,
                int activeMemberCount, boolean showMemberCount,
                List<RecentTopic> recentTopics) {
            this.activeTopicCount = activeTopicCount;
            this.activeReplyCount = activeReplyCount;
            this.activeCategoryCount = activeCategoryCount;
            this.activeMemberCount = activeMemberCount;
            this.showMemberCount = showMemberCount;
            this.recentTopics = recentTopics == null
                    ? new ArrayList<>()
                    : new ArrayList<>(recentTopics);
        }

        public int getActiveTopicCount() {
            return activeTopicCount;
        }

        public int getActiveReplyCount() {
            return activeReplyCount;
        }

        public int getActiveCategoryCount() {
            return activeCategoryCount;
        }

        public int getActiveMemberCount() {
            return activeMemberCount;
        }

        public boolean shouldShowMemberCount() {
            return showMemberCount;
        }

        public List<RecentTopic> getRecentTopics() {
            return new ArrayList<>(recentTopics);
        }
    }

    public static final class RecentTopic {
        private final long id;
        private final String title;
        private final String authorName;
        private final String categoryName;
        private final String createdAt;

        private RecentTopic(long id, String title, String authorName, String categoryName,
                String createdAt) {
            this.id = id;
            this.title = title;
            this.authorName = authorName;
            this.categoryName = categoryName;
            this.createdAt = createdAt;
        }

        public long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthorName() {
            return authorName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
