package com.example.campusforum.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.model.Topic;
import com.example.campusforum.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    public interface OnTopicClickListener {
        void onTopicClick(Topic topic);
    }

    private final List<Topic> topics = new ArrayList<>();
    private final OnTopicClickListener clickListener;

    public TopicAdapter(List<Topic> topics) {
        this(topics, null);
    }

    public TopicAdapter(List<Topic> topics, OnTopicClickListener clickListener) {
        this.clickListener = clickListener;
        updateTopics(topics);
    }

    public void updateTopics(List<Topic> updatedTopics) {
        topics.clear();
        if (updatedTopics != null) {
            topics.addAll(updatedTopics);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        holder.bind(topics.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {

        private final TextView categoryText;
        private final TextView titleText;
        private final TextView previewText;
        private final TextView authorText;
        private final TextView metaText;

        TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryText = itemView.findViewById(R.id.item_topic_category);
            titleText = itemView.findViewById(R.id.item_topic_title);
            previewText = itemView.findViewById(R.id.item_topic_preview);
            authorText = itemView.findViewById(R.id.item_topic_author);
            metaText = itemView.findViewById(R.id.item_topic_meta);
        }

        void bind(Topic topic, OnTopicClickListener clickListener) {
            // Safe null checks for robustness
            String category = safeText(topic.getCategory());
            if (category.isEmpty()) {
                category = itemView.getContext().getString(R.string.cf_category_unknown);
            }
            categoryText.setText(category);

            titleText.setText(safeText(topic.getTitle()));

            String preview = safeText(topic.getPreview());
            if (preview.isEmpty()) {
                preview = itemView.getContext().getString(R.string.cf_topic_no_preview);
            }
            previewText.setText(preview);

            String authorName = safeText(topic.getAuthorName());
            if (authorName.isEmpty()) {
                authorName = itemView.getContext().getString(R.string.cf_author_unknown);
            }
            authorText.setText(itemView.getContext().getString(
                    R.string.cf_author_format,
                    authorName));

            String replyLabel = topic.getReplyCount() > 1
                    ? itemView.getContext().getString(R.string.cf_topic_detail_replies_label)
                    : itemView.getContext().getString(R.string.cf_topic_detail_reply_label);
            metaText.setText(itemView.getContext().getString(
                    R.string.cf_topic_meta_format,
                    DateUtils.formatRelativeDate(topic.getCreatedAt()),
                    topic.getReplyCount(),
                    replyLabel));

            itemView.setOnClickListener(view -> {
                if (clickListener != null) {
                    clickListener.onTopicClick(topic);
                }
            });
        }

        private String safeText(String value) {
            return value == null ? "" : value;
        }
    }
}
