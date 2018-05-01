package com.task.omnify.appomnify.Adapters;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.task.omnify.appomnify.Models.Comment;
import com.task.omnify.appomnify.R;
import com.task.omnify.appomnify.Utils.TimeHelper;

import java.util.ArrayList;
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ArticleViewHolder> {
    private ArrayList<Comment> comments;
    private Context mContext;

    public CommentListAdapter(Context context, ArrayList<Comment> comments) {
        mContext = context;
        this.comments = comments;
    }

    @Override
    public CommentListAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        ArticleViewHolder viewHolder = new ArticleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CommentListAdapter.ArticleViewHolder holder, int position) {
        Comment comment= comments.get(position);
        holder.article_by_when.setText(TimeHelper.toPrettyDate(Long.parseLong(comment.getTime()))+" . "+comment.getBy());
        holder.comment.setText(comment.getText());


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView article_by_when,comment;


        public ArticleViewHolder(View itemView) {
            super(itemView);

            article_by_when = itemView.findViewById(R.id.article_by_when);
            comment = itemView.findViewById(R.id.comment);

        }
    }





}

