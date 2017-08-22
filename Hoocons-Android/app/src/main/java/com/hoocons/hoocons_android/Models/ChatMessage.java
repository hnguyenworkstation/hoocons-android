package com.hoocons.hoocons_android.Models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 8/6/17.
 */
@IgnoreExtraProperties
public class ChatMessage implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("message_type")
    private String messageType;
    @SerializedName("created_time")
    private String createdTime;
    @SerializedName("text_content")
    private String textContent;
    @SerializedName("content_url")
    private String contentUrl;
    @SerializedName("is_seen")
    private boolean isSeen;
    @SerializedName("contact_number")
    private String phoneNumber;
    @SerializedName("contact_name")
    private String contactName;
    @SerializedName("contact_profile_url")
    private String contactProfileUrl;
    @SerializedName("is_updated")
    private boolean isUpdated;
    @SerializedName("is_seen_by")
    private List<Integer> isSeenBy;

    private boolean isPosted;
    private boolean isShownName;
    private boolean shouldShowTimeHeader;

    public ChatMessage() {
    }

    public ChatMessage(int userId, String messageType, String createdTime,
                       String textContent, String contentUrl, boolean isSeen,
                       String phoneNumber, String contactName,
                       String contactProfileUrl, boolean isUpdated) {
        this.userId = userId;
        this.messageType = messageType;
        this.createdTime = createdTime;
        this.textContent = textContent;
        this.contentUrl = contentUrl;
        this.isSeen = isSeen;
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
        this.contactProfileUrl = contactProfileUrl;
        this.isUpdated = isUpdated;
    }

    public List<Integer> getIsSeenBy() {
        return isSeenBy;
    }

    public void setIsSeenBy(List<Integer> isSeenBy) {
        this.isSeenBy = isSeenBy;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public boolean isPosted() {
        return isPosted;
    }

    public void setPosted(boolean posted) {
        isPosted = posted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactProfileUrl() {
        return contactProfileUrl;
    }

    public void setContactProfileUrl(String contactProfileUrl) {
        this.contactProfileUrl = contactProfileUrl;
    }

    public boolean isShownName() {
        return isShownName;
    }

    public void setShownName(boolean shownName) {
        isShownName = shownName;
    }

    public boolean isShouldShowTimeHeader() {
        return shouldShowTimeHeader;
    }

    public void setShouldShowTimeHeader(boolean shouldShowTimeHeader) {
        this.shouldShowTimeHeader = shouldShowTimeHeader;
    }
}
