package com.example.campusforum.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.model.Reply;
import com.example.campusforum.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private static final int MENU_EDIT = 1;
    private static final int MENU_DELETE = 2;

    public interface OnReplyActionListener {
        void onEditReply(Reply reply);

        void onDeleteReply(Reply reply);
    }

    private final List<Reply> replies = new ArrayList<>();
    private final OnReplyActionListener actionListener;
    private long currentUserId = -1;
    private boolean currentUserAdmin;

    public ReplyAdapter(OnReplyActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void updateReplies(List<Reply> updatedReplies) {
        replies.clear();
        if (updatedReplies != null) {
            replies.addAll(updatedReplies);
        }
        notifyDataSetChanged();
    }

    public void updatePermissions(long currentUserId, boolean currentUserAdmin) {
        this.currentUserId = currentUserId;
        this.currentUserAdmin = currentUserAdmin;
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
        holder.bind(replies.get(position), currentUserId, currentUserAdmin, actionListener);
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    static class ReplyViewHolder extends RecyclerView.ViewHolder {

        private final TextView authorText;
        private final TextView createdAtText;
        private final TextView contentText;
        private final Button actionsButton;

        ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            authorText = itemView.findViewById(R.id.item_reply_author);
            createdAtText = itemView.findViewById(R.id.item_reply_created_at);
            contentText = itemView.findViewById(R.id.item_reply_content);
            actionsButton = itemView.findViewById(R.id.item_reply_actions_button);
        }

        void bind(Reply reply, long currentUserId, boolean currentUserAdmin,
                OnReplyActionListener actionListener) {
            authorText.setText(itemView.getContext().getString(
                    R.string.cf_author_format,
                    reply.getAuthorName()));
            createdAtText.setText(DateUtils.formatRelativeDate(reply.getCreatedAt()));
            contentText.setText(reply.getContent());

            boolean canManageReply = currentUserId > 0 &&
                    (currentUserAdmin || reply.getAuthorId() == currentUserId);
            actionsButton.setVisibility(canManageReply ? View.VISIBLE : View.GONE);
            actionsButton.setOnClickListener(canManageReply
                    ? view -> showReplyMenu(reply, actionListener)
                    : null);
        }

        private void showReplyMenu(Reply reply, OnReplyActionListener actionListener) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), actionsButton);
            popupMenu.getMenu().add(0, MENU_EDIT, 0, R.string.cf_action_edit);
            popupMenu.getMenu().add(0, MENU_DELETE, 1, R.string.cf_action_delete);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (actionListener == null) {
                    return true;
                }
                if (item.getItemId() == MENU_EDIT) {
                    actionListener.onEditReply(reply);
                    return true;
                }
                if (item.getItemId() == MENU_DELETE) {
                    actionListener.onDeleteReply(reply);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        }
    }
}
