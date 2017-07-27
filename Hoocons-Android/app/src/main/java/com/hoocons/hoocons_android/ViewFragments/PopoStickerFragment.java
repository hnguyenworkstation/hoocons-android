package com.hoocons.hoocons_android.ViewFragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;

import com.hoocons.hoocons_android.EventBus.AllowSlideDown;
import com.hoocons.hoocons_android.EventBus.BlockSlideDown;
import com.hoocons.hoocons_android.Interface.OnStickerChildInteractionListener;
import com.hoocons.hoocons_android.Interface.OnStickerPagerFragmentInteractionListener;
import com.hoocons.hoocons_android.Models.Emotion;
import com.hoocons.hoocons_android.Models.Emotions;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.SQLite.EmotionsDB;

import org.aisen.android.network.task.TaskException;
import org.aisen.android.ui.fragment.AGridFragment;
import org.aisen.android.ui.fragment.adapter.ARecycleViewItemView;
import org.aisen.android.ui.fragment.itemview.IITemView;
import org.aisen.android.ui.fragment.itemview.IItemViewCreator;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopoStickerFragment extends AGridFragment<Emotion, Emotions>
        implements OnItemClickListener, OnItemLongClickListener {

    private OnPoPoEmotionSelectedListener onPoPoEmotionSelectedListener;
    private boolean isGridViewScrolling = false;
    private OnStickerPagerFragmentInteractionListener mListener;
    private OnStickerChildInteractionListener mChildListener;
    private int myLastVisiblePos;

    public PopoStickerFragment() {
        // Required empty public constructor
    }

    public static PopoStickerFragment newInstance() {
        PopoStickerFragment fragment = new PopoStickerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int inflateContentView() {
        return R.layout.popo_gridview_layout;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        myLastVisiblePos = getRefreshView().getFirstVisiblePosition();

        getRefreshView().setOnItemClickListener(this);
        getRefreshView().setOnItemLongClickListener(this);
        getRefreshView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isGridViewScrolling = true;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    EventBus.getDefault().post(new AllowSlideDown());
                } else if (firstVisibleItem > 0) {
                    EventBus.getDefault().post(new BlockSlideDown());
                }

                int currentFirstVisPos = view.getFirstVisiblePosition();
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

        refreshGridView();
    }

    private void refreshGridView() {
        int gridViewEntrySize = getResources().getDimensionPixelSize(R.dimen.popo_image_size);
        int gridViewSpacing = getResources().getDimensionPixelSize(R.dimen.popo_image_padding);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int numColumns = (display.getWidth() - gridViewSpacing) / (gridViewEntrySize + gridViewSpacing);

        getRefreshView().setNumColumns(numColumns);
    }

    @Override
    public IItemViewCreator<Emotion> configItemViewCreator() {
        return new IItemViewCreator<Emotion>() {
            @Override
            public View newContentView(LayoutInflater inflater, ViewGroup parent, int viewType) {
                return inflater.inflate(R.layout.popo_sticker_viewholder, parent, false);
            }

            @Override
            public IITemView<Emotion> newItemView(View convertView, int viewType) {
                return new PoPoStickerItemView(convertView);
            }
        };
    }

    @Override
    public void requestData(RefreshMode var1) {
        new PoPoEmotionTask(var1).execute("d_");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showMessage(getAdapterItems().get(position).getKey());
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onPoPoEmotionSelectedListener != null)
            onPoPoEmotionSelectedListener.onPoPoEmotionSelected(getAdapterItems().get(position));
    }

    public class PoPoStickerItemView extends ARecycleViewItemView<Emotion> {
        @BindView(R.id.popoEmotion)
        ImageView popoEmotion;

        public PoPoStickerItemView(View itemView) {
            super(getActivity(), itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindData(View convertView, Emotion data, int position) {
            popoEmotion.setImageBitmap(BitmapFactory.decodeByteArray(data.getData(), 0, data.getData().length));
        }
    }

    public class PoPoEmotionTask extends APagingTask<String, Void, Emotions> {
        PoPoEmotionTask(RefreshMode mode) {
            super(mode);
        }

        @Override
        protected List<Emotion> parseResult(Emotions result) {
            return result.getEmotions();
        }

        @Override
        protected Emotions workInBackground(RefreshMode mode, String previousPage,
                                            String nextPage, String... params)
                throws TaskException {
            Emotions es = new Emotions();
            es.setEmotions(new ArrayList<Emotion>());

            Emotions emotions = EmotionsDB.getEmotions("popo_");
            es.getEmotions().addAll(emotions.getEmotions());

            return es;
        }
    }

    public void setOnEmotionListener(OnPoPoEmotionSelectedListener onPoPoEmotionSelectedListener) {
        this.onPoPoEmotionSelectedListener = onPoPoEmotionSelectedListener;
    }

    public interface OnPoPoEmotionSelectedListener {
        void onPoPoEmotionSelected(Emotion emotion);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshGridView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshGridView();
    }

}
