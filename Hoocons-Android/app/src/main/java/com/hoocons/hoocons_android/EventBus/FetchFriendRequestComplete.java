package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.ApiViewSets.FriendshipRequestApiViewSet;

/**
 * Created by hungnguyen on 8/26/17.
 */

public class FetchFriendRequestComplete {
    private FriendshipRequestApiViewSet friendshipRequestApiViewSet;

    public FetchFriendRequestComplete(FriendshipRequestApiViewSet friendshipRequestApiViewSet) {
        this.friendshipRequestApiViewSet = friendshipRequestApiViewSet;
    }

    public FriendshipRequestApiViewSet getFriendshipRequestApiViewSet() {
        return friendshipRequestApiViewSet;
    }

    public void setFriendshipRequestApiViewSet(FriendshipRequestApiViewSet friendshipRequestApiViewSet) {
        this.friendshipRequestApiViewSet = friendshipRequestApiViewSet;
    }
}
