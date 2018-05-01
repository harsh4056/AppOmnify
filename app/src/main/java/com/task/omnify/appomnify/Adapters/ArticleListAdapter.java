package com.task.omnify.appomnify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.task.omnify.appomnify.Activities.SliderActivity;
import com.task.omnify.appomnify.Models.Article;
import com.task.omnify.appomnify.Models.ArticleRealm;
import com.task.omnify.appomnify.R;
import com.task.omnify.appomnify.Utils.Constants;
import com.task.omnify.appomnify.Utils.TimeHelper;

import java.util.ArrayList;
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {
    private ArrayList<ArticleRealm> articleRealms;
    private Context mContext;

    public ArticleListAdapter(Context context, ArrayList<ArticleRealm> articleRealms) {
        mContext = context;
        this.articleRealms = articleRealms;
    }

    @Override
    public ArticleListAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_details, parent, false);
        ArticleViewHolder viewHolder = new ArticleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ArticleListAdapter.ArticleViewHolder holder,final int position) {
        final ArticleRealm articleRealm= articleRealms.get(position);
        holder.titletv.setText(articleRealm.getTitle());
        holder.urltv.setText(articleRealm.getUrl());
        holder.usertv.setText(TimeHelper.toDuration(articleRealm.getTime())+"."+articleRealm.getBy());
        holder.commentCounttv.setText(articleRealm.getDescendants()+"");
        holder.votesButton.setText(articleRealm.getScore()+"");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(v.getContext(), SliderActivity.class);
                Article article= new Article(articleRealms.get(position));
                i.putExtra(Constants.ID,article.getId());
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleRealms.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView titletv, urltv, usertv, commentCounttv;
        Button votesButton;
        CardView cardView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card);
            titletv = itemView.findViewById(R.id.article_title);
            urltv = itemView.findViewById(R.id.url);
            usertv = itemView.findViewById(R.id.time_username);
            commentCounttv = itemView.findViewById(R.id.comment_count);
            votesButton = itemView.findViewById(R.id.votes);
        }
    }


    public void updateData(ArrayList<ArticleRealm> articleRealms) {
        for(ArticleRealm articleRealm:articleRealms){
            this.articleRealms.add(articleRealm);
        }

        notifyDataSetChanged();
    }
    public void addItem(int position, ArticleRealm articleRealm) {
        articleRealms.add(position, articleRealm);
        notifyItemInserted(position);
    }

    public void removeItems() {
        articleRealms.clear();
        notifyDataSetChanged();

    }


}

