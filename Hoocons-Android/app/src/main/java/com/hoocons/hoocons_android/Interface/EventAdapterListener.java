package com.hoocons.hoocons_android.Interface;

import android.view.View;

/**
 * Created by hungnguyen on 7/18/17.
 */

public interface EventAdapterListener {
    void onLikeClicked(int position);

    void onCommentClicked(int position);

    void onShareClicked(final View view, int position);

    void onPhotoClicked(int position);

    void onVideoClicked(int position);

    void onWebThumbClicked(int position);

    void onEventImageClicked(int eventPos, int imagePos);
}
