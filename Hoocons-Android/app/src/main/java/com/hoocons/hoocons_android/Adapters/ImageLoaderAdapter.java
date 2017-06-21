package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.SquaredImageViewHolder;

import org.lucasr.twowayview.TwoWayLayoutManager;
import org.lucasr.twowayview.widget.SpannableGridLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;

/**
 * Created by hNguyen on 6/19/2017.
 */
public class ImageLoaderAdapter extends RecyclerView.Adapter<SquaredImageViewHolder> {
    private ArrayList<String> imageList;
    private Context context;
    private final TwoWayView mTwoWayView;

    public ImageLoaderAdapter(Context context, ArrayList<String> imageList, TwoWayView twoWayView) {
        this.imageList = imageList;
        this.context =context;
        this.mTwoWayView = twoWayView;
    }

    @Override
    public SquaredImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_square_image,
                parent, false);
        return new SquaredImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SquaredImageViewHolder holder, int position) {

        boolean isVertical = (mTwoWayView.getOrientation()
                == TwoWayLayoutManager.Orientation.VERTICAL);
        holder.initImage(context, imageList.get(position), position);

        View itemViewIs = holder.itemView;
        Integer itemId = position;

        final SpannableGridLayoutManager.LayoutParams lp =
                (SpannableGridLayoutManager.LayoutParams) itemViewIs.getLayoutParams();

        final int span1 = (itemId == 0 || itemId == 3 ? 2 : 1);
        final int span2 = (itemId == 0 ? 2 : (itemId == 3 ? 3 : 1));

        final int colSpan = (isVertical ? span2 : span1);
        final int rowSpan = (isVertical ? span1 : span2);

        if (lp.rowSpan != rowSpan || lp.colSpan != colSpan) {
            lp.rowSpan = rowSpan;
            lp.colSpan = colSpan;
            itemViewIs.setLayoutParams(lp);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
