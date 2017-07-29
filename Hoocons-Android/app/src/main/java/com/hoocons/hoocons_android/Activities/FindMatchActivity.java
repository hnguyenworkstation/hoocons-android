package com.hoocons.hoocons_android.Activities;

import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hoocons.hoocons_android.Adapters.UserFindMatchAdapter;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.BaseModel;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.CardEntity;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.SwipeFlingBottomLayout;
import com.hoocons.hoocons_android.DumbData.TestUserData;
import com.hoocons.hoocons_android.Helpers.RetrofitHelper;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.king.view.flutteringlayout.FlutteringLayout;
import com.zc.swiple.SwipeFlingView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class FindMatchActivity extends BaseActivity implements SwipeFlingView.OnSwipeFlingListener,
        SwipeFlingBottomLayout.OnBottomItemClickListener, SwipeFlingView.OnItemClickListener {
    private final static String TAG = FindMatchActivity.class.getSimpleName();
    private final static boolean DEBUG = true;

    @BindView(R.id.frame)
    SwipeFlingView mSwipeFlingView;
    @BindView(R.id.swipe_fling_bottom)
    SwipeFlingBottomLayout mBottomFlingLayout;
    @BindView(R.id.flutteringLayout)
    FlutteringLayout mFlutteringLayout;

    private int mPageIndex = 0;
    private boolean mIsRequestGirlList;
    private ArrayList<CardEntity> mGrilList = new ArrayList<>();

    private UserFindMatchAdapter mAdapter;
    private final Handler handler = new Handler();
    private final int delay = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);
        ButterKnife.bind(this);

        initView();
        requestGirlList();
    }

    private void initView() {
        mAdapter = new UserFindMatchAdapter(this, mGrilList);
        mSwipeFlingView.setAdapter(mAdapter);
        mSwipeFlingView.setOnSwipeFlingListener(this);
        mSwipeFlingView.setOnItemClickListener(this);
        mBottomFlingLayout.setOnBottomItemClickListener(this);

        mFlutteringLayout.bringToFront();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFlutteringLayout.addHeart();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void updateListView(ArrayList<CardEntity> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mGrilList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private void requestGirlList() {
        if (mIsRequestGirlList) {
            return;
        }
        mIsRequestGirlList = true;
        Call<BaseModel<ArrayList<CardEntity>>> call = RetrofitHelper.api().getGirlList(mPageIndex);
        RetrofitHelper.call(call, new RetrofitHelper.ApiCallback<ArrayList<CardEntity>>() {
            @Override
            public void onLoadSucceed(ArrayList<CardEntity> result) {
                updateListView(result);
                ++mPageIndex;
                mIsRequestGirlList = false;
            }

            @Override
            public void onLoadFail(int statusCode) {
                mIsRequestGirlList = false;
                Toast.makeText(getBaseContext(), "API Failed", Toast.LENGTH_LONG).show();
                addTestData();
            }

            @Override
            public void onForbidden() {
                mIsRequestGirlList = false;
            }
        });
    }

    private void addTestData() {
        updateListView(TestUserData.getApiData(getBaseContext()));
    }

    @Override
    public void onStartDragCard() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onStartDragCard");
        }
    }

    @Override
    public void onPreCardExit() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onPreCardExit");
        }
    }

    @Override
    public void onTopCardViewFinish() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onTopCardViewFinish");
        }
    }

    @Override
    public boolean canLeftCardExit() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView canLeftCardExit");
        }
        return true;
    }

    @Override
    public boolean canRightCardExit() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView canRightCardExit");
        }
        return true;
    }

    @Override
    public void onLeftCardExit(View view, Object dataObject, boolean triggerByTouchMove) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onLeftCardExit");
        }
    }

    @Override
    public void onRightCardExit(View view, Object dataObject, boolean triggerByTouchMove) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onRightCardExit");
        }
    }

    @Override
    public void onSuperLike(View view, Object dataObject, boolean triggerByTouchMove) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onSuperLike");
        }
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onAdapterAboutToEmpty");
        }
        requestGirlList();
    }

    @Override
    public void onAdapterEmpty() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onAdapterEmpty");
        }
    }

    @Override
    public void onScroll(View selectedView, float scrollProgressPercent) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onScroll " + scrollProgressPercent);
        }
    }

    @Override
    public void onEndDragCard() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onEndDragCard");
        }
    }

    @Override
    public void onComeBackClick() {
        mSwipeFlingView.selectComeBackCard(true);
    }

    @Override
    public void onSuperLikeClick() {
        if (mSwipeFlingView.isAnimationRunning()) {
            return;
        }
        mSwipeFlingView.selectSuperLike(false);
    }

    @Override
    public void onLikeClick() {
        if (mSwipeFlingView.isAnimationRunning()) {
            return;
        }
        mSwipeFlingView.selectRight(false);
    }

    @Override
    public void onUnLikeClick() {
        if (mSwipeFlingView.isAnimationRunning()) {
            return;
        }
        mSwipeFlingView.selectLeft(false);
    }

    @Override
    public void onItemClicked(int itemPosition, Object dataObject) {
        if (DEBUG) {
            Log.d(TAG, "onItemClicked itemPosition:" + itemPosition);
        }
    }
}

