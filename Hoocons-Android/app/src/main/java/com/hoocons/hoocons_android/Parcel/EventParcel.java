package com.hoocons.hoocons_android.Parcel;

import org.parceler.Parcel;

/**
 * Created by hungnguyen on 7/22/17.
 */
@Parcel
public class EventParcel {
    int id;
    int likeCount;
    boolean isLiked;

    public EventParcel() {

    }

    public EventParcel(int id, int likeCount, boolean isLiked) {
        this.id = id;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
