package com.hoocons.hoocons_android.ViewFragments;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * 表情窗口
 *
 * @author wangdan
 *
 */
public class EmotionFragment extends AGridFragment<Emotion, Emotions>
        implements OnItemClickListener, OnItemLongClickListener {

    private boolean isGridViewScrolling = false;
    private OnStickerPagerFragmentInteractionListener mListener;
    private OnStickerChildInteractionListener mChildListener;
    private int myLastVisiblePos;

    public static EmotionFragment newInstance() {
        return new EmotionFragment();
    }

    private OnEmotionSelectedListener onEmotionSelectedListener;

    @Override
    public int inflateContentView() {
        return R.layout.emotion_layout;
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onEmotionSelectedListener != null)
            onEmotionSelectedListener.onEmotionSelected(getAdapterItems().get(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showMessage(getAdapterItems().get(position).getKey());
        return true;
    }

    @Override
    public IItemViewCreator<Emotion> configItemViewCreator() {
        return new IItemViewCreator<Emotion>() {

            @Override
            public View newContentView(LayoutInflater inflater, ViewGroup parent, int viewType) {
                return inflater.inflate(R.layout.emotion_item, parent, false);
            }

            @Override
            public IITemView<Emotion> newItemView(View convertView, int viewType) {
                return new EmotionItemView(convertView);
            }

        };
    }

    @Override
    public void requestData(RefreshMode mode) {
        new EmotionTask(mode).execute("d_");
    }

    public class EmotionItemView extends ARecycleViewItemView<Emotion> {
        @BindView(R.id.imgEmotion)
        ImageView imgEmotion;

        public EmotionItemView(View itemView) {
            super(getActivity(), itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindData(View convertView, Emotion data, int position) {
            imgEmotion.setImageBitmap(BitmapFactory.decodeByteArray(data.getData(), 0, data.getData().length));
        }
    }

    public class EmotionTask extends APagingTask<String, Void, Emotions> {
        public EmotionTask(RefreshMode mode) {
            super(mode);
        }

        @Override
        protected List<Emotion> parseResult(Emotions result) {
            return result.getEmotions();
        }

        @Override
        protected Emotions workInBackground(RefreshMode mode, String previousPage, String nextPage, String... params)
                throws TaskException {
            Emotions es = new Emotions();
            es.setEmotions(new ArrayList<Emotion>());

            Emotions emotions = EmotionsDB.getEmotions("d_");
            es.getEmotions().addAll(emotions.getEmotions());
            emotions = EmotionsDB.getEmotions("hs_");
            es.getEmotions().addAll(emotions.getEmotions());
            emotions = EmotionsDB.getEmotions("h_");
            es.getEmotions().addAll(emotions.getEmotions());
            emotions = EmotionsDB.getEmotions("f_");
            es.getEmotions().addAll(emotions.getEmotions());
            emotions = EmotionsDB.getEmotions("o_");
            es.getEmotions().addAll(emotions.getEmotions());
            emotions = EmotionsDB.getEmotions("w_");
            es.getEmotions().addAll(emotions.getEmotions());

            return es;
        }
    }

    public void setOnEmotionListener(OnEmotionSelectedListener onEmotionSelectedListener) {
        this.onEmotionSelectedListener = onEmotionSelectedListener;
    }

    public interface OnEmotionSelectedListener {
        void onEmotionSelected(Emotion emotion);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}