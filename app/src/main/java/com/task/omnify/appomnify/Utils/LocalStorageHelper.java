package com.task.omnify.appomnify.Utils;

import com.task.omnify.appomnify.Interfaces.ArticleResponseListener;
import com.task.omnify.appomnify.Interfaces.DataFetchListener;
import com.task.omnify.appomnify.Interfaces.TopStoriesListListener;
import com.task.omnify.appomnify.Models.Article;
import com.task.omnify.appomnify.Models.ArticleRealm;
import com.task.omnify.appomnify.Network.RequestData;

import java.util.ArrayList;

import io.realm.Realm;

public class LocalStorageHelper {

    DataFetchListener dataFetchListener;
    int total=0;
    int count=0;
    public void fetchArticleList(DataFetchListener dataFetchListener){
        this.dataFetchListener = dataFetchListener;

        RequestData.requestTopStoriesList(new TopStoriesListListener() {
            @Override
            public void onStoriesRecieved(ArrayList<Long> stories) {
                storeArticlesLocally(stories);
            }

            @Override
            public void onFailure(String failureMessage) {

            }
        });
    }


    Realm realm = Realm.getInstance(AppController.getInstance());
    public void storeArticlesLocally(ArrayList<Long> articleIDs){
        total=articleIDs.size();
        count=0;
        final  ArticleRepository articleRepository= new ArticleRepository();
        for(long articleID:articleIDs){
            if(!isArticleStored(articleID)){
            RequestData.requestArticle(articleID + "", new ArticleResponseListener() {
                @Override
                public void onArticleRetrieved(Article article) {
                    articleRepository.addNewData(article);
                    count++;
                    dataFetchListener.onFetchProgressUpdate((count/total)*100);
                    if(count==total)
                    dataFetchListener.onFetchingComplete();
                }

                @Override
                public void onFailure(String failureMessage) {

                }
            });
            }
            else{
                count++;
                dataFetchListener.onFetchProgressUpdate((count*100/total));
                if(count==total)
                    dataFetchListener.onFetchingComplete();
            }
        }

    }
    private boolean isArticleStored(long id){
        ArticleRealm articleRealm = realm.where(ArticleRealm.class).equalTo("id", id).findFirst();
        if (articleRealm != null)
            return true;
        return false;
    }
    public ArrayList<ArticleRealm> retrieveArticlesCurrent(){
        long unixTime = System.currentTimeMillis() / 1000L;
        return retrieveArticlesByGivenTime(unixTime);
    }
    public ArrayList<ArticleRealm> retrieveArticlesByGivenTime(long time){
        final  ArticleRepository articleRepository= new ArticleRepository();
        return articleRepository.getDataBytime(time);
    }







}
