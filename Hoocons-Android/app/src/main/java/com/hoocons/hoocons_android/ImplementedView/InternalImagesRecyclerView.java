package com.hoocons.hoocons_android.ImplementedView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hoocons.hoocons_android.R;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.adapter.PhotoGridAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.event.OnPhotoClickListener;
import me.iwf.photopicker.fragment.ImagePagerFragment;
import me.iwf.photopicker.fragment.PhotoPickerFragment;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import me.iwf.photopicker.utils.MediaStoreHelper;

import static android.widget.Toast.LENGTH_LONG;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * Created by hungnguyen on 8/23/17.
 */
public class InternalImagesRecyclerView extends RelativeLayout {
    private View view;
    private Context context;

    private int SCROLL_THRESHOLD = 30;
    private int MAX_COUNT = 6;
    private final int MAX_ROWS = 3;
    private RequestManager mGlideRequestManager;
    private PhotoGridAdapter photoGridAdapter;

    public InternalImagesRecyclerView(Context context) {
        super(context, null);
        this.context = context;
    }

    public InternalImagesRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(FragmentActivity activity) {
        mGlideRequestManager = Glide.with(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.merge_recycler_layout, this);
        setBackgroundColor(getResources().getColor(R.color.white));

        RecyclerView recyclerView = view.findViewById(R.id.merge_recycler);
        final List<PhotoDirectory> directories = new ArrayList<>();
        photoGridAdapter = new PhotoGridAdapter(activity, mGlideRequestManager,
                directories, null, MAX_ROWS);
        photoGridAdapter.setShowCamera(false);
        photoGridAdapter.setPreviewEnable(false);

        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, true);
        MediaStoreHelper.getPhotoDirs(activity, mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override public void onResultCallback(List<PhotoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        photoGridAdapter.notifyDataSetChanged();
                    }
                });

        initClickListener(photoGridAdapter, activity);
        initRecyclerView(recyclerView, photoGridAdapter);
    }

    private void initClickListener(final PhotoGridAdapter photoGridAdapter, final FragmentActivity activity) {
        photoGridAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean onItemCheck(int position, Photo path, int selectedItemCount) {
                if (MAX_COUNT <= 1) {
                    List<String> photos = photoGridAdapter.getSelectedPhotos();
                    if (!photos.contains(path.getPath())) {
                        photos.clear();
                        photoGridAdapter.notifyDataSetChanged();
                    }
                    return true;
                }

                if (selectedItemCount > MAX_COUNT) {
                    Toast.makeText(activity,
                            context.getResources()
                                    .getString(me.iwf.photopicker.R.string.__picker_over_max_count_tips,
                                            MAX_COUNT),
                            LENGTH_LONG).show();
                    return false;
                }

                return true;
            }
        });
    }

    public List<String> getPhotoPathsSelected() {
        return photoGridAdapter.getSelectedPhotos();
    }

    private void initRecyclerView(RecyclerView recyclerView, final PhotoGridAdapter photoGridAdapter) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(MAX_ROWS,
                OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    mGlideRequestManager.pauseRequests();
                } else {
                    resumeRequestsIfNotDestroyed();
                }
            }
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resumeRequestsIfNotDestroyed();
                }
            }
        });
    }

    private void resumeRequestsIfNotDestroyed() {
        if (!AndroidLifecycleUtils.canLoadImage(context)) {
            return;
        }

        mGlideRequestManager.resumeRequests();
    }
}
