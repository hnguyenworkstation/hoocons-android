package com.hoocons.hoocons_android.EventBus;

import java.util.List;

/**
 * Created by hungnguyen on 8/15/17.
 */

public class TagsCollected {
    private List<String> tags;

    public TagsCollected(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
