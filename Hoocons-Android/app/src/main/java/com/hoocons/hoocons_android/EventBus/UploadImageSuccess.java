package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 8/16/17.
 */

public class UploadImageSuccess {
    private String url;
    private String tag;

    public UploadImageSuccess(String url, String tag) {
        this.url = url;
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
