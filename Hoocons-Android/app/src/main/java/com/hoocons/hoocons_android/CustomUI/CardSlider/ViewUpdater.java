package com.hoocons.hoocons_android.CustomUI.CardSlider;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by hungnguyen on 7/29/17.
 */

public abstract class ViewUpdater {
    protected final CardSliderLayoutManager lm;

    public ViewUpdater(CardSliderLayoutManager lm) {
        this.lm = lm;
    }

    public void onLayoutManagerInitialized() {}

    abstract public int getActiveCardPosition();

    abstract @Nullable public View getTopView();

    abstract public void updateView();

}
