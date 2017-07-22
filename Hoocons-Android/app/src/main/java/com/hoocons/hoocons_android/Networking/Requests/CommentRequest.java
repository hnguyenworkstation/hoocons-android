package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 7/21/17.
 */

public class CommentRequest {
    @SerializedName("text_content")
    private String textContent;
    @SerializedName("comment_type")
    private String commentType;
    @SerializedName("comment_media_url")
    private String commentMediaUrl;

    public CommentRequest(String textContent, String commentType, String commentMediaUrl) {
        this.textContent = textContent;
        this.commentType = commentType;
        this.commentMediaUrl = commentMediaUrl;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getCommentMediaUrl() {
        return commentMediaUrl;
    }

    public void setCommentMediaUrl(String commentMediaUrl) {
        this.commentMediaUrl = commentMediaUrl;
    }
}
