package com.task.omnify.appomnify.Interfaces;

import com.task.omnify.appomnify.Models.Article;

public interface ArticleResponseListener extends ResponseListener {
    void onArticleRetrieved(Article article);


}
