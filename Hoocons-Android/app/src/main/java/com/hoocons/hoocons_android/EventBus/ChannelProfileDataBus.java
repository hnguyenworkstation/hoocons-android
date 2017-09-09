package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Parcel.ChannelProfileParcel;

/**
 * Created by hungnguyen on 9/9/17.
 */

public class ChannelProfileDataBus {
    private ChannelProfileParcel channelProfileParcel;

    public ChannelProfileDataBus(ChannelProfileParcel channelProfileParcel) {
        this.channelProfileParcel = channelProfileParcel;
    }

    public ChannelProfileParcel getChannelProfileParcel() {
        return channelProfileParcel;
    }

    public void setChannelProfileParcel(ChannelProfileParcel channelProfileParcel) {
        this.channelProfileParcel = channelProfileParcel;
    }
}
