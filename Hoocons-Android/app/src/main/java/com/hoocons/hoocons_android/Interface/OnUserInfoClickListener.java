package com.hoocons.hoocons_android.Interface;

import com.hoocons.hoocons_android.Parcel.MeetOutParcel;

/**
 * Created by hungnguyen on 7/30/17.
 */

public interface OnUserInfoClickListener {
    void onUserProfileClicked();
    void onAddFriendClicked();
    void onEditProfileClicked();
    void onStartChatClicked();
    void onProfileMoreClicked();
    void onMeetOutViewClicked(final MeetOutParcel meetOutParcel);
    void onViewMoreMeetOutClicked();
}
