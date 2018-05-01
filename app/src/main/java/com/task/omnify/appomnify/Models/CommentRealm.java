package com.task.omnify.appomnify.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CommentRealm extends RealmObject {
    private String by;
    private String text;
    private String type;
    private String time;
    private String kids;
    @PrimaryKey
    private String id;
    private String parent;


    public void setType(String type) {
        this.type = type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public String getBy() {
        return by;
    }

    public String getId() {
        return id;
    }

    public String getParent() {
        return parent;
    }

    public String getTime() {
        return time;
    }

    public String getKids() {
        return kids;
    }

    public String getText() {
        return text;
    }
}
