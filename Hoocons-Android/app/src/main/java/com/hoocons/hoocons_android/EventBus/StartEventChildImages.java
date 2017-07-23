package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 7/23/17.
 */

public class StartEventChildImages {
    private int eventPosition;
    private int imagePosition;

    public StartEventChildImages(int eventPosition, int imagePosition) {
        this.eventPosition = eventPosition;
        this.imagePosition = imagePosition;
    }

    public int getEventPosition() {
        return eventPosition;
    }

    public void setEventPosition(int eventPosition) {
        this.eventPosition = eventPosition;
    }

    public int getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(int imagePosition) {
        this.imagePosition = imagePosition;
    }
}
