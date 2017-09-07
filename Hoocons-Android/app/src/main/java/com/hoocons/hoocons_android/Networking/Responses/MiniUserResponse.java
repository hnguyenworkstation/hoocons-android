package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 9/6/17.
 */

public class MiniUserResponse {
    @SerializedName("user")
    private int user;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("profile_url")
    private String profileUrl;
    @SerializedName("wallpaper_url")
    private String wallpaperUrl;
    @SerializedName("last_action_at")
    private String lastActionAt;
    @SerializedName("is_sharing_location")
    private boolean isSharingLocation;
}
