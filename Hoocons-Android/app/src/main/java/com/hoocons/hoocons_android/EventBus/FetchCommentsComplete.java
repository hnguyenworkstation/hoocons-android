package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.Responses.CommentResponse;

import java.util.List;

/**
 * Created by hungnguyen on 7/22/17.
 */

public class FetchCommentsComplete {
    private List<CommentResponse> commentResponseList;

    public FetchCommentsComplete(List<CommentResponse> commentResponseList) {
        this.commentResponseList = commentResponseList;
    }

    public List<CommentResponse> getCommentResponseList() {
        return commentResponseList;
    }

    public void setCommentResponseList(List<CommentResponse> commentResponseList) {
        this.commentResponseList = commentResponseList;
    }
}
