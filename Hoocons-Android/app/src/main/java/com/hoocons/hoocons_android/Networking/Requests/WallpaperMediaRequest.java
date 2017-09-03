package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Media;

/**
 * Created by hungnguyen on 9/3/17.
 */

public class WallpaperMediaRequest {
    @SerializedName("wallpaper_media")
    private Media wallpaper;

    public WallpaperMediaRequest(Media wallpaper) {
        this.wallpaper = wallpaper;
    }

    public Media getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(Media wallpaper) {
        this.wallpaper = wallpaper;
    }
}
