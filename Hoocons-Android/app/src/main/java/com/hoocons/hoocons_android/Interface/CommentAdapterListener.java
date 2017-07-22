package com.hoocons.hoocons_android.Interface;

/**
 * Created by hungnguyen on 7/21/17.
 */

public interface CommentAdapterListener {
    void onCommentLikeClicked(final int position);
    void onCommentReplyClicked(final int position);
    void onCommentViewClicked(final int position);
    void onCommentProfileClicked(final int position);
    void onCommentNameClicked(final int position);
    void onCommentLongClicked(final int position);
}
