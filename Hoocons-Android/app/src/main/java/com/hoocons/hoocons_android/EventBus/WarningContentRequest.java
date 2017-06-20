package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hNguyen on 6/20/2017.
 */

public class WarningContentRequest {
    private boolean isRequested = false;

    public WarningContentRequest(boolean isRequested) {
        this.isRequested = isRequested;
    }

    public boolean isRequested() {
        return isRequested;
    }

    public void setRequested(boolean requested) {
        isRequested = requested;
    }
}
