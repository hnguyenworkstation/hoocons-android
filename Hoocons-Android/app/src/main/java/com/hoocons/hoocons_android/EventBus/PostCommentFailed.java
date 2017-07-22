package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 7/22/17.
 */

public class PostCommentFailed {
    private String commentTag;

    public PostCommentFailed(String commentTag) {
        this.commentTag = commentTag;
    }

    public String getCommentTag() {
        return commentTag;
    }

    public void setCommentTag(String commentTag) {
        this.commentTag = commentTag;
    }
}
