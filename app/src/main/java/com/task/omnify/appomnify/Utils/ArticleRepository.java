package com.task.omnify.appomnify.Utils;

import com.task.omnify.appomnify.Interfaces.Repository;
import com.task.omnify.appomnify.Models.Article;
import com.task.omnify.appomnify.Models.ArticleRealm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class ArticleRepository implements Repository {
    Realm realm= Realm.getInstance(AppController.getInstance());
    static int LIMIT = 10;
    @Override
    public Object getDataById(String id) {
        return null;
    }

    @Override
    public ArrayList<ArticleRealm> getDataByRange(long time) {

        final RealmResults<ArticleRealm> realmResults = realm.
                 where(ArticleRealm.class)
                .findAllSorted("time",false);
        ArrayList<ArticleRealm> articleRealms =new ArrayList<>();
        for(int i=0;i<realmResults.size();i++){
            articleRealms.add(realmResults.get(i));
        }
        return  articleRealms;

    }


    public ArrayList<ArticleRealm> getDataBytime(long time) {

        final RealmResults<ArticleRealm> realmResults = realm.
                where(ArticleRealm.class)
                .lessThan("time",time)
                .findAllSorted("time",false);
        ArrayList<ArticleRealm> articleRealms =new ArrayList<>();
        for(int i=0;i<10;i++){
            articleRealms.add(realmResults.get(i));
        }
        return  articleRealms;

    }


    @Override
    public void addNewData(Object o) {
       Article article= (Article) o;

        realm.beginTransaction();
        ArticleRealm articleRealm = realm.createObject(ArticleRealm.class);
        articleRealm.setBy(article.getBy());
        articleRealm.setDescendants(article.getDescendants()+"");
        articleRealm.setId(article.getId());
        articleRealm.setKids(article.getKids());
        articleRealm.setScore(article.getScore()+"");
        articleRealm.setTime(article.getTime());
        articleRealm.setTitle(article.getTitle());
        articleRealm.setUrl(article.getUrl());
        articleRealm.setType(article.getType());
        realm.commitTransaction();

    }



    public void updateData(Object o) {
        Article article= (Article) o;

        realm.beginTransaction();
        ArticleRealm articleRealm = realm.where(ArticleRealm.class).equalTo(Constants.ID,article.getId()).findFirst();
        articleRealm.setBy(article.getBy());
        articleRealm.setDescendants(article.getDescendants()+"");
        articleRealm.setKids(article.getKids());
        articleRealm.setScore(article.getScore()+"");
        articleRealm.setTime(article.getTime());
        articleRealm.setTitle(article.getTitle());
        articleRealm.setUrl(article.getUrl());
        articleRealm.setType(article.getType());
        realm.commitTransaction();

    }
}
