package com.task.omnify.appomnify.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import com.task.omnify.appomnify.Fragments.CommentsFragment;
import com.task.omnify.appomnify.Fragments.WebViewFragment;
import com.task.omnify.appomnify.Interfaces.ArticleResponseListener;
import com.task.omnify.appomnify.Models.Article;
import com.task.omnify.appomnify.Network.RequestData;
import com.task.omnify.appomnify.R;
import com.task.omnify.appomnify.Utils.ArticleRepository;
import com.task.omnify.appomnify.Utils.Constants;
import com.task.omnify.appomnify.Utils.TimeHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SliderActivity extends BaseActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView articletv,timetv,urltv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        articletv=findViewById(R.id.article_title);
        timetv=findViewById(R.id.time_username);
        urltv=findViewById(R.id.url);
        long id=getIntent().getLongExtra(Constants.ID,1L);
        //To refetch article data and update locally and retrieve
        //Comments pointer
        RequestData.requestArticle(id + "", new ArticleResponseListener() {
            @Override
            public void onArticleRetrieved(Article article) {
                setAdapters(article);
                articletv.setText(article.getTitle());
                urltv.setText(article.getUrl());
                timetv.setText(TimeHelper.toPrettyDate(article.getTime())+" . "+article.getBy());
                //Update data for article when fetching comments,this can be used at other places suitably
                ArticleRepository articleRepository= new ArticleRepository();
                articleRepository.updateData(article);
            }

            @Override
            public void onFailure(String failureMessage) {

            }
        });


    }




    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void setAdapters(Article article){
        viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(CommentsFragment.newInstance(article), article.getDescendants()+" COMMENTS");
        if(!article.getUrl().equals(""))
            adapter.addFragment(WebViewFragment.getInstance(article.getUrl()), "ARTICLE");
        viewPager.setAdapter(adapter);
        tabLayout =  findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

}