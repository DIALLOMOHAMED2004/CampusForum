package com.example.campusforum.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.repository.DashboardRepository;
import com.example.campusforum.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class DashboardRecentTopicAdapter
        extends RecyclerView.Adapter<DashboardRecentTopicAdapter.RecentTopicViewHolder> {

    public interface OnRecentTopicClickListener {
        void onRecentTopicClick(DashboardRepository.RecentTopic topic);
    }

    private final List<DashboardRepository.RecentTopic> recentTopics = new ArrayList<>();
    private final OnRecentTopicClickListener clickListener;

    public DashboardRecentTopicAdapter(OnRecentTopicClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateTopics(List<DashboardRepository.RecentTopic> topics) {
        recentTopics.clear();
        if (topics != null) {
            recentTopics.addAll(topics);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecentTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard_recent_topic, parent, false);
        return new RecentTopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTopicViewHolder holder, int position) {
        holder.bind(recentTopics.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return recentTopics.size();
    }

    static class RecentTopicViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleText;
        private final TextView categoryText;
        private final TextView metaText;

        RecentTopicViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.item_dashboard_recent_topic_title);
            categoryText = itemView.findViewById(R.id.item_dashboard_recent_topic_category);
            metaText = itemView.findViewById(R.id.item_dashboard_recent_topic_meta);
        }

        void bind(DashboardRepository.RecentTopic topic, OnRecentTopicClickListener clickListener) {
            titleText.setText(safeText(topic.getTitle()));

            String categoryName = safeText(topic.getCategoryName());
            if (categoryName.isEmpty()) {
                categoryName = itemView.getContext().getString(R.string.cf_category_unknown);
            }
            categoryText.setText(categoryName);

            String authorName = safeText(topic.getAuthorName());
            if (authorName.isEmpty()) {
                authorName = itemView.getContext().getString(R.string.cf_author_unknown);
            }
            metaText.setText(itemView.getContext().getString(
                    R.string.cf_dashboard_recent_topic_meta,
                    itemView.getContext().getString(
                            R.string.cf_author_format,
                            authorName),
                    DateUtils.formatRelativeDate(topic.getCreatedAt())));

            itemView.setOnClickListener(view -> {
                if (clickListener != null) {
                    clickListener.onRecentTopicClick(topic);
                }
            });
        }

        private String safeText(String value) {
            return value == null ? "" : value;
        }
    }
}
