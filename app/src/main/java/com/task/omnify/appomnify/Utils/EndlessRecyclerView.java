package com.task.omnify.appomnify.Utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.task.omnify.appomnify.Interfaces.LoadMoreListener;

public class EndlessRecyclerView extends RecyclerView{
    LoadMoreListener loadMoreListener;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public EndlessRecyclerView(Context context) {
        super(context);
    }


    public void addLoadMoreListener(final LoadMoreListener loadMoreListener, final LinearLayoutManager mLayoutManager){
        this.loadMoreListener=loadMoreListener;

        this.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            loadMoreListener.onLoadMore();
                            loading=true;
                        }
                    }
                }
            }
        });


    }
    public EndlessRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EndlessRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
