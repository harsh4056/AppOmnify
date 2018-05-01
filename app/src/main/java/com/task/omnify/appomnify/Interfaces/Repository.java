package com.task.omnify.appomnify.Interfaces;

import com.task.omnify.appomnify.Models.ArticleRealm;

import java.util.ArrayList;

public interface Repository {
    Object getDataById(String id);
    ArrayList<ArticleRealm> getDataByRange(long time);
    void addNewData(Object o);
}
