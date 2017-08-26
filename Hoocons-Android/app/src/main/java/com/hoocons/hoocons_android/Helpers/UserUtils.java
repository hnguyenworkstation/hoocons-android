package com.hoocons.hoocons_android.Helpers;

import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Responses.SemiUserInfoResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.Parcel.UserParcel;

/**
 * Created by hungnguyen on 8/23/17.
 */

public class UserUtils {
    public static UserParcel getUserParcelFromSemiResponse(SemiUserInfoResponse response) {
        UserParcel parcel = new UserParcel();
        parcel.setFriend(response.isFriend());
        parcel.setUserId(response.getUser());
        parcel.setHobbies(response.getHobbies());
        parcel.setUserDisplayName(response.getDisplayName());
        parcel.setUserNickname(response.getNickname());
        parcel.setUserProfileUrl(response.getProfileUrl());
        parcel.setUserWallpaperUrl(response.getWallpaperUrl());
        parcel.setFriendRequested(response.isFriendRequested());

        return parcel;
    }

    public static UserParcel getSelfParcel() {
        SharedPreferencesManager pref = SharedPreferencesManager.getDefault();
        UserParcel parcel = new UserParcel();
        parcel.setUserId(pref.getUserId());
        parcel.setUserDisplayName(pref.getUserDisplayName());
        parcel.setUserNickname(pref.getUserNickname());
        parcel.setUserProfileUrl(pref.getUserProfileUrl());
        parcel.setUserWallpaperUrl(pref.getUserProfileUrl());

        return parcel;
    }

    public static UserInfoResponse getFakeUserResponse(UserParcel parcel) {
        UserInfoResponse response = new UserInfoResponse();
        response.setUserPK(parcel.getUserId());
        response.setHobbies(parcel.getHobbies());
        response.setNickname(parcel.getUserNickname());
        response.setDisplayName(parcel.getUserDisplayName());
        response.setProfileUrl(parcel.getUserProfileUrl());
        response.setWallPaperUrl(parcel.getUserWallpaperUrl());
        response.setFriend(parcel.isFriend());
        response.setFriendRequested(parcel.isFriendRequested());
        return response;
    }
}
