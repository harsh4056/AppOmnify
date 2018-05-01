package com.task.omnify.appomnify.Interfaces;

import com.task.omnify.appomnify.Models.Comment;

public interface CommentResponeListener extends ResponseListener {
    void onCommentRecieved(Comment comment);

}
