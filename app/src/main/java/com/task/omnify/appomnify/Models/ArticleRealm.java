package com.task.omnify.appomnify.Models;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ArticleRealm extends RealmObject {
    private String by;
    private String title;
    private String type;
    private String url;
    private long time;
    @Ignore
    private String kids;
    private String descendants;
    private String score;
    @PrimaryKey
    private long id;

    public String getDescendants() {
        return descendants;
    }

    public long getId() {
        return id;
    }

    public String getScore() {
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

    public void setDescendants(String descendants) {
        this.descendants = descendants;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public void setScore(String score) {
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




}
