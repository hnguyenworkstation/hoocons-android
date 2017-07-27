package com.hoocons.hoocons_android.ViewFragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.hoocons.hoocons_android.Adapters.StickersAdapter;
import com.hoocons.hoocons_android.EventBus.AllowSlideDown;
import com.hoocons.hoocons_android.EventBus.BlockSlideDown;
import com.hoocons.hoocons_android.Interface.OnStickerChildInteractionListener;
import com.hoocons.hoocons_android.Interface.OnStickerClickListener;
import com.hoocons.hoocons_android.Interface.OnStickerPagerFragmentInteractionListener;
import com.hoocons.hoocons_android.Models.Sticker;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.SQLite.StickerDB;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonkeyStickerFragment extends Fragment implements OnStickerClickListener{
    @BindView(R.id.monkey_recycler)
    ObservableRecyclerView mMonkeyRecycler;

    private List<Sticker> mMonkeyListSticker;
    private StickersAdapter stickersAdapter;

    private boolean isGridViewScrolling = false;
    private OnStickerPagerFragmentInteractionListener mListener;
    private OnStickerChildInteractionListener mChildListener;
    private int myLastVisiblePos;

    public MonkeyStickerFragment() {
        // Required empty public constructor
    }

    public static MonkeyStickerFragment newInstance() {
        MonkeyStickerFragment fragment = new MonkeyStickerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        mMonkeyListSticker = StickerDB.getMonkeyStickerSet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monkey_sticker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        stickersAdapter = new StickersAdapter(getContext(), this, mMonkeyListSticker);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getEstimatedColumns(),
                LinearLayoutManager.VERTICAL, false);
        mMonkeyRecycler.setLayoutManager(layoutManager);
        mMonkeyRecycler.setAdapter(stickersAdapter);
        mMonkeyRecycler.setItemAnimator(new DefaultItemAnimator());
        mMonkeyRecycler.setNestedScrollingEnabled(false);
        mMonkeyRecycler.setVisibility(View.VISIBLE);
        mMonkeyRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                isGridViewScrolling = true;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                int currentFirstVisPos = layoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem == 0) {
                    EventBus.getDefault().post(new AllowSlideDown());
                } else if (firstVisibleItem > 0) {
                    EventBus.getDefault().post(new BlockSlideDown());
                }

                if (!isGridViewScrolling) {
                    myLastVisiblePos = currentFirstVisPos;
                    return;
                }
                if (mListener != null) {
                    mChildListener.resetCountUpAnimation();
                }
                if (firstVisibleItem == 0) {
                    if (currentFirstVisPos > myLastVisiblePos) {
                        if (mListener != null) {
                            mChildListener.onScrollDown(true);
                        }
                    }
                    myLastVisiblePos = currentFirstVisPos;
                    return;
                }

                if (currentFirstVisPos > myLastVisiblePos) {
                    if (mListener != null) {
                        mChildListener.onScrollDown(true);
                    }
                }
                if (currentFirstVisPos < myLastVisiblePos) {
                    if (mListener != null) {
                        mChildListener.onScrollDown(false);
                    }
                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });
    }

    private int getEstimatedColumns() {
        int gridViewEntrySize = getResources().getDimensionPixelSize(R.dimen.sticker_image_size);
        int gridViewSpacing = getResources().getDimensionPixelSize(R.dimen.popo_image_padding);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return (display.getWidth() - gridViewSpacing) / (gridViewEntrySize + gridViewSpacing);
    }

    @Override
    public void onStickerClick(int position) {

    }

    @Override
    public void onStickerLongClick(int position) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnStickerPagerFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        try {
            mChildListener = (OnStickerChildInteractionListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnChildInteractionListener");
        }
    }
}
