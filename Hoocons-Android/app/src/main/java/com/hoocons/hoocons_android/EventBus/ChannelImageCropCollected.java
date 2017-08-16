package com.hoocons.hoocons_android.EventBus;

import android.net.Uri;

/**
 * Created by hungnguyen on 8/16/17.
 */

public class ChannelImageCropCollected {
    private Uri uri;

    public ChannelImageCropCollected(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
