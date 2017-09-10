package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.ApiViewSets.ChannelApiViewSet;

/**
 * Created by hungnguyen on 9/10/17.
 */

public class FetchOwnedChannelsComplete {
    private ChannelApiViewSet channelApiViewSet;

    public FetchOwnedChannelsComplete(ChannelApiViewSet channelApiViewSet) {
        this.channelApiViewSet = channelApiViewSet;
    }

    public ChannelApiViewSet getChannelApiViewSet() {
        return channelApiViewSet;
    }

    public void setChannelApiViewSet(ChannelApiViewSet channelApiViewSet) {
        this.channelApiViewSet = channelApiViewSet;
    }
}
