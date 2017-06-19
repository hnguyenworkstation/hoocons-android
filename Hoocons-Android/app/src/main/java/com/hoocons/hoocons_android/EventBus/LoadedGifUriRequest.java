package com.hoocons.hoocons_android.EventBus;

import android.net.Uri;

/**
 * Created by hNguyen on 6/19/2017.
 */

public class LoadedGifUriRequest {
    private Uri content;

    public LoadedGifUriRequest(Uri content) {
        this.content = content;
    }

    public Uri getGifUri() {
        return content;
    }
}
