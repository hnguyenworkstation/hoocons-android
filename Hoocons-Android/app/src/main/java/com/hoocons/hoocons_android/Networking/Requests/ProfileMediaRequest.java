package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Media;

/**
 * Created by hungnguyen on 9/3/17.
 */

public class ProfileMediaRequest {
    @SerializedName("profile_media")
    private Media mProfileMedia;

    public ProfileMediaRequest(Media mProfileMedia) {
        this.mProfileMedia = mProfileMedia;
    }

    public Media getProfileMedia() {
        return mProfileMedia;
    }

    public void setProfileMedia(Media mProfileMedia) {
        this.mProfileMedia = mProfileMedia;
    }
}
