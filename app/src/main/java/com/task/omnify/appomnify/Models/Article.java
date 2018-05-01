package com.task.omnify.appomnify.Models;

import java.io.Serializable;

public class Article implements Serializable{
    private  String by,title,type,url,kids;
    private long descendants,score,id,time;

    public long getDescendants() {
        return descendants;
    }

    public long getId() {
        return id;
    }

    public long getScore() {
        return score;
    }

    public String getKids() {
        return kids;
    }

    public String getBy() {
        return by;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setDescendants(long descendants) {
        this.descendants = descendants;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public  Article(ArticleRealm articleRealm){
        this.by=articleRealm.getBy();
        String descendants=articleRealm.getDescendants();
        if(descendants.equals(""))
        this.descendants= 0;
        else
        this.descendants= Long.parseLong(descendants);
        this.id=articleRealm.getId();
        this.score= Long.parseLong(articleRealm.getScore());
        this.title=articleRealm.getTitle();
        this.kids=articleRealm.getKids();
        this.time=articleRealm.getTime();
        this.type=articleRealm.getType();
        this.url=articleRealm.getUrl();
    }
    public Article(){

    }
}

