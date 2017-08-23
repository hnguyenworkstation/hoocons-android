package com.hoocons.hoocons_android.Interface;

import android.view.View;

/**
 * Created by hungnguyen on 7/18/17.
 */

public interface EventAdapterListener {
    void onEventHeaderClicked(final int position);

    void onUserInfoClicked(final int position);

    void onSharedUserInfoClicked(final int position);

    void onLikeClicked(final int position);

    void onCommentClicked(final int position);

    void onShareClicked(final View view, int position);

    void onPhotoClicked(final int position);

    void onVideoClicked(final int position);

    void onWebThumbClicked(final int position);

    void onEventImageClicked(final int eventPos, final int imagePos);

    void onOptionClicked(View view, final int position);
}
