package com.task.omnify.appomnify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.task.omnify.appomnify.Adapters.ArticleListAdapter;
import com.task.omnify.appomnify.Interfaces.DataFetchListener;
import com.task.omnify.appomnify.Interfaces.LoadMoreListener;
import com.task.omnify.appomnify.Models.ArticleRealm;
import com.task.omnify.appomnify.R;
import com.task.omnify.appomnify.Utils.Constants;
import com.task.omnify.appomnify.Utils.EndlessRecyclerView;
import com.task.omnify.appomnify.Utils.LocalStorageHelper;
import com.task.omnify.appomnify.Utils.StoredVariables;
import com.task.omnify.appomnify.Utils.TimeHelper;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener ,LoadMoreListener{
    private FirebaseAuth mAuth;
    private ArticleListAdapter mAdapter;
    private EndlessRecyclerView mRecyclerView;
    private TextView topStories,updatedAgo;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LocalStorageHelper localStorageHelper;
    private long lastRefreshAt,lastDataAt;
    private GoogleSignInClient mGoogleSignInClient;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        progressBar=findViewById(R.id.progressBar);
        topStories=findViewById(R.id.top_stories);
        updatedAgo=findViewById(R.id.updated_ago);
        mRecyclerView=findViewById(R.id.articleRecyclerView);
        ArrayList<ArticleRealm> articleRealms= new ArrayList<>();
        mAdapter = new ArticleListAdapter(getApplicationContext(), articleRealms);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addLoadMoreListener(this, (LinearLayoutManager) layoutManager);
        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(MainActivity.this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        lastRefreshAt=System.currentTimeMillis();
        localStorageHelper = new LocalStorageHelper();
        showProgressBar();

        loadRecyclerViewData();
        final Handler handler = new Handler();
        //Thread to calculate time ago since last refreshed
        handler.post(new Runnable() {
            @Override
            public void run() {
                updatedAgo.setText(TimeHelper.toDuration(lastRefreshAt/1000));
                handler.postDelayed(this,1000);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_tab_slider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onStart();
    }


    @Override
    public void onClick(View v) {


    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void loadRecyclerViewData(){
        localStorageHelper.fetchArticleList( new DataFetchListener() {
            @Override
            public void onFetchingComplete() {
                hideProgressBar();
                lastRefreshAt=System.currentTimeMillis();
                UIdataClean(lastRefreshAt);
            }

            @Override
            public void onFetchProgressUpdate(int progress) {
            }
        });
    }

    //THis method signs out accordingly to the method stored in the login activity
    private void signOut() {
        mAuth.signOut();
        int signInMethod= StoredVariables.getSignInMethod();
        switch (signInMethod) {
            case Constants.CASE_GOOGLE:
            mAuth.signOut();
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
            break;
            case Constants.CASE_FACEBOOK:
            //Facebook sign out
            LoginManager.getInstance().logOut();
            break;
            default:

        }
        StoredVariables.storeSignInMethod(Constants.CASE_LOGOUT);
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onLoadMore() {
    UIdataAdd(lastDataAt);
    }

    //Add more data to be called when scroll is reached at bottom
    public void UIdataAdd(long time){
        ArrayList<ArticleRealm> articleRealms= localStorageHelper.retrieveArticlesByGivenTime(time);
        lastDataAt= articleRealms.get(articleRealms.size()-1).getTime();
        Log.d("size_of_data",articleRealms.size()+"");
        mAdapter.updateData(articleRealms);
    }
    //Clear data when refreshed and add again
    public void UIdataClean(long time){
        ArrayList<ArticleRealm> articleRealms= localStorageHelper.retrieveArticlesByGivenTime(time);
        lastDataAt= articleRealms.get(articleRealms.size()-1).getTime();
        Log.d("size_of_data",articleRealms.size()+"");
        mAdapter.removeItems();
        mAdapter.updateData(articleRealms);
    }

}
