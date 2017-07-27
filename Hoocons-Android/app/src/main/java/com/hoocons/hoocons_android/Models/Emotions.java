package com.hoocons.hoocons_android.Models;

/**
 * Created by hungnguyen on 6/24/17.
 */
import java.io.Serializable;
import java.util.List;

public class Emotions implements Serializable {
    private static final long serialVersionUID = 6179327495902944739L;
    private List<Emotion> emotions;
    public List<Emotion> getEmotions() {
        return emotions;
    }
    public void setEmotions(List<Emotion> emotions) {
        this.emotions = emotions;
    }
}