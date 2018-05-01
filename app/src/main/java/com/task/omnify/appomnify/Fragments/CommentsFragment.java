package com.task.omnify.appomnify.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.task.omnify.appomnify.Activities.MainActivity;
import com.task.omnify.appomnify.Adapters.ArticleListAdapter;
import com.task.omnify.appomnify.Adapters.CommentListAdapter;
import com.task.omnify.appomnify.Interfaces.CommentResponeListener;
import com.task.omnify.appomnify.Interfaces.DataFetchListener;
import com.task.omnify.appomnify.Models.Article;
import com.task.omnify.appomnify.Models.ArticleRealm;
import com.task.omnify.appomnify.Models.Comment;
import com.task.omnify.appomnify.Network.RequestData;
import com.task.omnify.appomnify.R;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;

public class CommentsFragment extends Fragment {
    Article article;
    int total;
    int count;
    private ProgressBar progressBar;
    DataFetchListener dataFetchListener;
    private RecyclerView mRecyclerView;
    private CommentListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comments, container, false);
        progressBar=v.findViewById(R.id.progressBar);
       getAllComments(getAllCommentIds(article.getKids()),v,dataFetchListener);
        return v;
    }

    public static CommentsFragment newInstance(Article article) {
        CommentsFragment f = new CommentsFragment();
        f.article=article;
        return f;
    }
        //Process comment ids and extract
    public String[] getAllCommentIds(String comments){
        comments=comments.replace("[","");
        comments=comments.replace("]","");
        String IDs[]=comments.split(",");
        return IDs;
    }
        //Fetch all comments
    public void getAllComments(String [] IDs,final View v, final DataFetchListener dataFetchListener){

        final ArrayList<Comment> comments= new ArrayList<>();
        total = IDs.length;

        count = 0;
        for( int i=0;i<total;i++){
            progressBar.setVisibility(View.VISIBLE);
            RequestData.requestComment(IDs[i], new CommentResponeListener() {
                @Override
                public void onCommentRecieved(Comment comment) {
                    count++;

                    comments.add(comment);
                    if(count==total) {
                        //Add all comments
                        mRecyclerView=v.findViewById(R.id.commentRecyclerView);
                        mAdapter = new CommentListAdapter(v.getContext(), comments);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(v.getContext());
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                        progressBar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(String failureMessage) {
                    count++;
                    if(count==total) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }






    }
}
