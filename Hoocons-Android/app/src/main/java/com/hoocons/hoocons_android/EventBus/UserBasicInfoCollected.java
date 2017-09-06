package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 9/5/17.
 */

public class UserBasicInfoCollected {
    private String profileUrl;
    private String displayName;
    private String gender;
    private String birthday;

    public UserBasicInfoCollected(String profileUrl, String displayName, String gender, String birthday) {
        this.profileUrl = profileUrl;
        this.displayName = displayName;
        this.gender = gender;
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
