package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 7/30/17.
 */

public class OnMeetOutViewClicked {
    private int meetOutId;

    public OnMeetOutViewClicked(int meetOutId) {
        this.meetOutId = meetOutId;
    }

    public int getMeetOutId() {
        return meetOutId;
    }

    public void setMeetOutId(int meetOutId) {
        this.meetOutId = meetOutId;
    }
}
