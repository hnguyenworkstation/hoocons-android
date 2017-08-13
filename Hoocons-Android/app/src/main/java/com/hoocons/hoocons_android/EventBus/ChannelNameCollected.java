package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 8/12/17.
 */

public class ChannelNameCollected {
    private String name;

    public ChannelNameCollected(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
