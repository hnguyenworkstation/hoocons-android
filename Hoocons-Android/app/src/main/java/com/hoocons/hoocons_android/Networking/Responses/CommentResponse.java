package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 7/21/17.
 */

public class CommentResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("comment_by")
    private SemiUserInfoResponse commentBy;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("text_content")
    private String textContent;
    @SerializedName("comment_type")
    private String commentType;
    @SerializedName("comment_media_url")
    private String commentMediaUrl;
    @SerializedName("like_count")
    private int likeCount;
    @SerializedName("reply_count")
    private int replyCount;
    @SerializedName("is_liked")
    private boolean isLiked;

    private boolean isUploaded = true;
    private boolean isUploadFailed = false;
    private String commentTag;

    public CommentResponse(SemiUserInfoResponse commentBy,
                           String createdAt, String textContent,
                           String commentType, String commentMediaUrl,
                           int likeCount, int replyCount, boolean isLiked,
                           boolean isUploaded, boolean isUploadFailed,
                           String commentTag) {
        this.commentBy = commentBy;
        this.createdAt = createdAt;
        this.textContent = textContent;
        this.commentType = commentType;
        this.commentMediaUrl = commentMediaUrl;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.isLiked = isLiked;
        this.isUploaded = isUploaded;
        this.isUploadFailed = isUploadFailed;
        this.commentTag = commentTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SemiUserInfoResponse getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(SemiUserInfoResponse commentBy) {
        this.commentBy = commentBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getCommentTag() {
        return commentTag;
    }

    public void setCommentTag(String commentTag) {
        this.commentTag = commentTag;
    }

    public boolean isUploadFailed() {
        return isUploadFailed;
    }

    public void setUploadFailed(boolean uploadFailed) {
        isUploadFailed = uploadFailed;
    }
}
