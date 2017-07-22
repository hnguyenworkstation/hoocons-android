package com.hoocons.hoocons_android.Parcel;

import org.parceler.Parcel;

/**
 * Created by hungnguyen on 7/22/17.
 */
@Parcel
public class EventParcel {
    int id;
    int likeCount;
    int reportCount;
    boolean isLiked;

    public EventParcel() {

    }

    public EventParcel(int id, int likeCount, int reportCount, boolean isLiked) {
        this.id = id;
        this.likeCount = likeCount;
        this.reportCount = reportCount;
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

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
