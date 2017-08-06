package com.hoocons.hoocons_android.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 8/6/17.
 */

public class ChatMessage {
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
    private String isSeen;
    @SerializedName("contact_number")
    private String phoneNumber;
    @SerializedName("contact_name")
    private String contactName;
    @SerializedName("contact_profile_url")
    private String contactProfileUrl;

    public ChatMessage(int userId, String messageType, String createdTime,
                       String textContent, String contentUrl, String isSeen,
                       String phoneNumber, String contactName, String contactProfileUrl) {
        this.userId = userId;
        this.messageType = messageType;
        this.createdTime = createdTime;
        this.textContent = textContent;
        this.contentUrl = contentUrl;
        this.isSeen = isSeen;
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
        this.contactProfileUrl = contactProfileUrl;
    }

    public ChatMessage(int userId, String messageType, String createdTime,
                       String textContent, String contentUrl) {
        this.userId = userId;
        this.messageType = messageType;
        this.createdTime = createdTime;
        this.textContent = textContent;
        this.contentUrl = contentUrl;
    }

    public ChatMessage(int userId, String messageType, String createdTime) {
        this.userId = userId;
        this.messageType = messageType;
        this.createdTime = createdTime;
    }

    public ChatMessage(int userId, String messageType, String createdTime, String textContent) {
        this.userId = userId;
        this.messageType = messageType;
        this.createdTime = createdTime;
        this.textContent = textContent;
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

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
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
}
