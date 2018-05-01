package com.task.omnify.appomnify.Utils;

import com.task.omnify.appomnify.Interfaces.Repository;
import com.task.omnify.appomnify.Models.ArticleRealm;
import com.task.omnify.appomnify.Models.Comment;
import com.task.omnify.appomnify.Models.CommentRealm;

import java.util.ArrayList;

import io.realm.Realm;

public class CommentRepository implements Repository {
    @Override
    public Object getDataById(String id) {
        return null;
    }

    @Override
    public ArrayList<ArticleRealm> getDataByRange(long time) {
        return null;
    }


    @Override
    public void addNewData(Object o) {
        Comment comment= (Comment) o;
        Realm realm= Realm.getInstance(AppController.getInstance());
        realm.beginTransaction();
        CommentRealm commentRealm = realm.createObject(CommentRealm.class);
        commentRealm.setBy(comment.getBy());
        commentRealm.setId(comment.getId()+"");
        commentRealm.setKids(comment.getKids());
        commentRealm.setTime(comment.getTime());
        commentRealm.setType(comment.getType());
        commentRealm.setParent(comment.getParent());
        commentRealm.setText(comment.getText());
        realm.commitTransaction();
    }
}
