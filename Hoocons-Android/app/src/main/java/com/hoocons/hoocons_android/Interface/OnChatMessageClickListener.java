package com.hoocons.hoocons_android.Interface;

/**
 * Created by hungnguyen on 8/6/17.
 */

public interface OnChatMessageClickListener {
    void onMessageClickListener(final int position);
    void onMessageLocationClickListener(final int position);
    void onMessageImageClickListener(final int position);
    void onMessageContactClickListener(final int position);
}
