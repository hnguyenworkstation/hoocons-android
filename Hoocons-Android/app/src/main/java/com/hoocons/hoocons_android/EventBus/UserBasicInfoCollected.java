package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 9/5/17.
 */

public class UserBasicInfoCollected {
    private String profileUrl;
    private String displayName;
    private String gender;

    public UserBasicInfoCollected(String profileUrl, String displayName, String gender) {
        this.profileUrl = profileUrl;
        this.displayName = displayName;
        this.gender = gender;
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
