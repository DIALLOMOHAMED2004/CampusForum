package com.example.campusforum.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.model.Reply;
import com.example.campusforum.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private final List<Reply> replies = new ArrayList<>();

    public void updateReplies(List<Reply> updatedReplies) {
        replies.clear();
        if (updatedReplies != null) {
            replies.addAll(updatedReplies);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reply, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        holder.bind(replies.get(position));
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    static class ReplyViewHolder extends RecyclerView.ViewHolder {

        private final TextView authorText;
        private final TextView createdAtText;
        private final TextView contentText;

        ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            authorText = itemView.findViewById(R.id.item_reply_author);
            createdAtText = itemView.findViewById(R.id.item_reply_created_at);
            contentText = itemView.findViewById(R.id.item_reply_content);
        }

        void bind(Reply reply) {
            authorText.setText(itemView.getContext().getString(
                    R.string.cf_author_format,
                    reply.getAuthorName()));
            createdAtText.setText(DateUtils.formatRelativeDate(reply.getCreatedAt()));
            contentText.setText(reply.getContent());
        }
    }
}
