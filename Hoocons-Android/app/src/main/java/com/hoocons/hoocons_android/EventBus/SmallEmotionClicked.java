package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Models.Emotion;

/**
 * Created by hungnguyen on 7/26/17.
 */

public class SmallEmotionClicked {
    private Emotion emotion;

    public SmallEmotionClicked(Emotion emotion) {
        this.emotion = emotion;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }
}
