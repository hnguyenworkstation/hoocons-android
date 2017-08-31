package com.hoocons.hoocons_android.CustomUI;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hungnguyen on 8/30/17.
 */

public class StaggeredItemDecorator extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public StaggeredItemDecorator(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        outRect.top = mSpace;
    }
}