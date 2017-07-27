//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.aisen.android.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.aisen.android.R.id;
import org.aisen.android.common.context.GlobalContext;
import org.aisen.android.common.utils.ActivityHelper;
import org.aisen.android.common.utils.Logger;
import org.aisen.android.common.utils.ViewUtils;
import org.aisen.android.network.biz.IResult;
import org.aisen.android.network.task.TaskException;
import org.aisen.android.support.paging.IPaging;
import org.aisen.android.ui.fragment.adapter.IPagingAdapter;
import org.aisen.android.ui.fragment.adapter.IPagingAdapter.Utils;
import org.aisen.android.ui.fragment.itemview.AFooterItemView;
import org.aisen.android.ui.fragment.itemview.AHeaderItemViewCreator;
import org.aisen.android.ui.fragment.itemview.BasicFooterView;
import org.aisen.android.ui.fragment.itemview.IITemView;
import org.aisen.android.ui.fragment.itemview.IItemViewCreator;
import org.aisen.android.ui.fragment.itemview.OnFooterViewListener;
import org.aisen.android.ui.fragment.itemview.AFooterItemView.OnFooterViewCallback;
import org.aisen.android.ui.widget.AsToolbar.OnToolbarDoubleClick;

public abstract class APagingFragment<T extends Serializable,
        Ts extends Serializable, V extends ViewGroup>
        extends ABaseFragment implements OnToolbarDoubleClick,
        OnFooterViewListener, OnFooterViewCallback {
    private static final String TAG = "AFragment-Paging";
    public static final String PAGING_TASK_ID = "org.aisen.android.PAGING_TASK";
    private static final String SAVED_DATAS = "org.aisen.android.ui.Datas";
    private static final String SAVED_PAGING = "org.aisen.android.ui.Paging";
    private static final String SAVED_CONFIG = "org.aisen.android.ui.Config";
    private IPaging mPaging;
    private IPagingAdapter<T> mAdapter;
    private APagingFragment.APagingTask pagingTask;
    APagingFragment.RefreshConfig refreshConfig;
    IItemViewCreator<T> mFooterItemViewCreator;
    AFooterItemView<T> mFooterItemView;
    AHeaderItemViewCreator<T> mHeaderItemViewCreator;
    private boolean refreshViewScrolling = false;

    public APagingFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            this.refreshConfig = new APagingFragment.RefreshConfig();
        } else {
            this.refreshConfig = (APagingFragment.RefreshConfig)savedInstanceState.getSerializable("org.aisen.android.ui.Config");
        }

        ArrayList datas = savedInstanceState == null?new ArrayList():(ArrayList)savedInstanceState.getSerializable("org.aisen.android.ui.Datas");
        this.mAdapter = this.newAdapter(datas);
        if(savedInstanceState != null && savedInstanceState.getSerializable("org.aisen.android.ui.Paging") != null) {
            this.mPaging = (IPaging)savedInstanceState.getSerializable("org.aisen.android.ui.Paging");
        } else {
            this.mPaging = this.newPaging();
        }

    }

    public void onSaveInstanceState(Bundle outState) {
        if(this.mPaging != null) {
            outState.putSerializable("org.aisen.android.ui.Paging", this.mPaging);
        }

        if(this.refreshConfig != null) {
            outState.putSerializable("org.aisen.android.ui.Config", this.refreshConfig);
        }

        this.onSaveDatas(outState);
        super.onSaveInstanceState(outState);
    }

    protected void onSaveDatas(Bundle outState) {
        if(this.getAdapter() != null && this.getAdapter().getDatas().size() != 0) {
            outState.putSerializable("org.aisen.android.ui.Datas", this.getAdapter().getDatas());
        }

    }

    void _layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super._layoutInit(inflater, savedInstanceSate);
        this.setupRefreshConfig(this.refreshConfig);
        this.setupRefreshView(savedInstanceSate);
        this.setupRefreshViewWithConfig(this.refreshConfig);
        this.bindAdapter(this.getAdapter());
    }

    public IPagingAdapter getAdapter() {
        return this.mAdapter;
    }

    public ArrayList<T> getAdapterItems() {
        return this.mAdapter.getDatas();
    }

    public void onPullDownToRefresh() {
        this.requestData(APagingFragment.RefreshMode.refresh);
    }

    public void onPullUpToRefresh() {
        this.requestData(APagingFragment.RefreshMode.update);
    }

    public boolean isContentEmpty() {
        return this.getAdapter() == null || this.getAdapter().getDatas().size() == 0;
    }

    protected final void onTaskStateChanged(ABaseTaskState state, TaskException exception) {
    }

    protected void onTaskStateChanged(ABaseTaskState state, TaskException exception, APagingFragment.RefreshMode mode) {
        super.onTaskStateChanged(state, exception);
        this.onTaskStateChanged(this.mFooterItemView, state, exception, mode);
        if(state == ABaseTaskState.success) {
            if(this.isContentEmpty() && this.emptyLayout != null && !TextUtils.isEmpty(this.refreshConfig.emptyHint)) {
                ViewUtils.setTextViewValue(this.emptyLayout, id.txtLoadEmpty, this.refreshConfig.emptyHint);
            }
        } else if(state == ABaseTaskState.falid) {
            if(this.isContentEmpty() && this.loadFailureLayout != null && !TextUtils.isEmpty(exception.getMessage())) {
                ViewUtils.setTextViewValue(this.loadFailureLayout, id.txtLoadFailed, exception.getMessage());
            }
        } else if(state == ABaseTaskState.finished) {
            this.onRefreshViewFinished(mode);
        }

    }

    public void setAdapterItems(ArrayList<T> items) {
        this.mAdapter.getDatas().clear();
        this.mAdapter.getDatas().addAll(items);
    }

    protected void setupRefreshConfig(APagingFragment.RefreshConfig config) {
    }

    protected IPaging<T, Ts> newPaging() {
        return null;
    }

    public abstract IItemViewCreator<T> configItemViewCreator();

    public abstract void requestData(APagingFragment.RefreshMode var1);

    public abstract V getRefreshView();

    protected abstract IPagingAdapter<T> newAdapter(ArrayList<T> var1);

    protected abstract void bindAdapter(IPagingAdapter var1);

    public boolean setRefreshViewToLoading() {
        return false;
    }

    protected void setupRefreshViewWithConfig(APagingFragment.RefreshConfig config) {
    }

    public void onRefreshViewFinished(APagingFragment.RefreshMode mode) {
    }

    protected void setupRefreshView(Bundle savedInstanceSate) {
        if(this.refreshConfig != null && this.refreshConfig.footerMoreEnable) {
            this.mFooterItemViewCreator = this.configFooterViewCreator();
            View convertView = this.mFooterItemViewCreator.newContentView(this.getActivity().getLayoutInflater(), (ViewGroup)null, 2000);
            this.mFooterItemView = (AFooterItemView)this.mFooterItemViewCreator.newItemView(convertView, 2000);
        }

        this.mHeaderItemViewCreator = this.configHeaderViewCreator();
        if(this.mFooterItemView != null) {
            this.addFooterViewToRefreshView(this.mFooterItemView);
        }

        if(this.mHeaderItemViewCreator != null) {
            this.addHeaderViewToRefreshView(this.mHeaderItemViewCreator);
        }

    }

    public boolean isRefreshing() {
        return this.pagingTask != null;
    }

    public boolean onToolbarDoubleClick() {
        return false;
    }

    public void requestData() {
        APagingFragment.RefreshMode mode = APagingFragment.RefreshMode.reset;
        if(this.getAdapter().getDatas().size() == 0 && this.loadingLayout == null) {
            mode = APagingFragment.RefreshMode.update;
        }

        this.requestData(mode);
    }

    public void requestDataOutofdate() {
        this.putLastReadPosition(0);
        this.putLastReadTop(0);
        this.requestDataSetRefreshing();
    }

    public void requestDataSetRefreshing() {
        if(!this.isRefreshing() && !this.setRefreshViewToLoading()) {
            this.requestData(APagingFragment.RefreshMode.reset);
        }

    }

    public void requestDataDelaySetRefreshing(long delay) {
        Runnable requestDelayRunnable = new Runnable() {
            public void run() {
                Logger.d("AFragment-Paging", "延迟刷新，开始刷新, " + this.toString());
                APagingFragment.this.requestDataSetRefreshing();
            }
        };
        this.runUIRunnable(requestDelayRunnable, delay);
    }

    protected AHeaderItemViewCreator<T> configHeaderViewCreator() {
        return null;
    }

    protected IItemViewCreator<T> configFooterViewCreator() {
        return new IItemViewCreator() {
            public View newContentView(LayoutInflater inflater, ViewGroup parent, int viewType) {
                return inflater.inflate(BasicFooterView.LAYOUT_RES, parent, false);
            }

            public IITemView<T> newItemView(View convertView, int viewType) {
                return new BasicFooterView(APagingFragment.this.getActivity(), convertView, APagingFragment.this);
            }
        };
    }

    protected abstract void addFooterViewToRefreshView(AFooterItemView<?> var1);

    protected abstract void addHeaderViewToRefreshView(AHeaderItemViewCreator<?> var1);

    public void setFooterViewToRefreshing() {
        if(this.mFooterItemView != null) {
            this.mFooterItemView.setFooterViewToRefreshing();
        }

    }

    public void onTaskStateChanged(AFooterItemView<?> footerItemView, ABaseTaskState state, TaskException exception, APagingFragment.RefreshMode mode) {
        if(this.refreshConfig != null && this.refreshConfig.footerMoreEnable && this.mFooterItemView != null) {
            if(this.mFooterItemView != null) {
                this.mFooterItemView.onTaskStateChanged(footerItemView, state, exception, mode);
            }

        }
    }

    protected void onScrollStateChanged(int scrollState) {
        if(!this.refreshConfig.displayWhenScrolling) {
            if(scrollState == 2) {
                this.refreshViewScrolling = true;
            } else if(scrollState == 1) {
                this.refreshViewScrolling = true;
            } else if(scrollState == 0) {
                this.refreshViewScrolling = false;
            }
        }

        if(scrollState == 0 && !this.refreshConfig.pagingEnd && this.refreshConfig.footerMoreEnable && this.mFooterItemView != null) {
            int childCount = this.getRefreshView().getChildCount();
            if(childCount > 0 && this.getRefreshView().getChildAt(childCount - 1) == this.mFooterItemView.getConvertView()) {
                this.setFooterViewToRefreshing();
            }
        }

        if(scrollState == 0 && !TextUtils.isEmpty(this.refreshConfig.positionKey) && this.getRefreshView() != null) {
            this.runNUIRunnable(new Runnable() {
                public void run() {
                    APagingFragment.this.putLastReadPosition(APagingFragment.this.getFirstVisiblePosition());
                    APagingFragment.this.putLastReadTop(APagingFragment.this.getRefreshView().getChildAt(0).getTop());
                }
            });
        }

    }

    public boolean canLoadMore() {
        return !this.refreshConfig.pagingEnd;
    }

    public void onLoadMore() {
        this.onPullUpToRefresh();
    }

    protected void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public boolean canDisplay() {
        return this.refreshConfig.displayWhenScrolling?true:!this.refreshViewScrolling;
    }

    protected void toLastReadPosition() {
        if(this.getRefreshView() != null && !TextUtils.isEmpty(this.refreshConfig.positionKey)) {
            if(this.getRefreshView() instanceof ListView) {
                this.runUIRunnable(new Runnable() {
                    public void run() {
                        ListView listView = (ListView)APagingFragment.this.getRefreshView();
                        listView.setSelectionFromTop(APagingFragment.this.getLastReadPosition(), APagingFragment.this.getLastReadTop() + listView.getPaddingTop());
                    }
                });
            }

        }
    }

    protected int getLastReadPosition() {
        return ActivityHelper.getIntShareData(GlobalContext.getInstance(), this.refreshConfig.positionKey + "Position", 0);
    }

    protected void putLastReadPosition(int position) {
        if(!TextUtils.isEmpty(this.refreshConfig.positionKey)) {
            ActivityHelper.putIntShareData(GlobalContext.getInstance(), this.refreshConfig.positionKey + "Position", position);
        }

    }

    protected int getLastReadTop() {
        return ActivityHelper.getIntShareData(GlobalContext.getInstance(), this.refreshConfig.positionKey + "Top", 0);
    }

    protected void putLastReadTop(int top) {
        if(!TextUtils.isEmpty(this.refreshConfig.positionKey)) {
            ActivityHelper.putIntShareData(GlobalContext.getInstance(), this.refreshConfig.positionKey + "Top", top);
        }

    }

    protected int getFirstVisiblePosition() {
        return 0;
    }

    public void refreshUI() {
    }

    public void releaseImageViewByIds() {
    }

    public abstract class APagingTask<Params, Progress, Result extends Serializable> extends ABaseTask<Params, Progress, Result> {
        protected final APagingFragment.RefreshMode mode;

        public APagingTask(APagingFragment.RefreshMode mode) {
            super("org.aisen.android.PAGING_TASK");
            this.mode = mode;
            APagingFragment.this.pagingTask = this;
            if(mode == APagingFragment.RefreshMode.reset && APagingFragment.this.mPaging != null) {
                APagingFragment.this.mPaging = APagingFragment.this.newPaging();
            }

        }

        protected void onPrepare() {
            super.onPrepare();
            Logger.d("AFragment-Paging", this.toString() + "-" + ABaseTaskState.prepare + " - " + this.mode);
            APagingFragment.this.onTaskStateChanged(ABaseTaskState.prepare, (TaskException)null, this.mode);
        }

        public Result workInBackground(Params... params) throws TaskException {
            String previousPage = null;
            String nextPage = null;
            if(APagingFragment.this.mPaging != null) {
                previousPage = APagingFragment.this.mPaging.getPreviousPage();
                nextPage = APagingFragment.this.mPaging.getNextPage();
            }

            return this.workInBackground(this.mode, previousPage, nextPage, params);
        }

        protected void onSuccess(Result result) {
            if(result != null && APagingFragment.this.getActivity() != null) {
                APagingFragment.this.bindAdapter(APagingFragment.this.getAdapter());
                Object resultList;
                if(result instanceof List) {
                    resultList = (List)result;
                } else {
                    resultList = this.parseResult(result);
                    if(resultList == null) {
                        resultList = new ArrayList();
                    }
                }

                if(!this.handleResult(this.mode, (List)resultList) && this.mode == APagingFragment.RefreshMode.reset) {
                    APagingFragment.this.setAdapterItems(new ArrayList());
                }

                if(this.mode != APagingFragment.RefreshMode.reset && this.mode != APagingFragment.RefreshMode.refresh) {
                    if(this.mode == APagingFragment.RefreshMode.update) {
                        Utils.addItemsAndRefresh(APagingFragment.this.getAdapter(), (List)resultList);
                    }
                } else {
                    Utils.addItemsAtFrontAndRefresh(APagingFragment.this.getAdapter(), (List)resultList);
                }

                if(APagingFragment.this.mPaging != null) {
                    if(APagingFragment.this.getAdapter() != null && APagingFragment.this.getAdapter().getDatas().size() != 0) {
                        APagingFragment.this.mPaging.processData(result, (Serializable)APagingFragment.this.getAdapter().getDatas().get(0), (Serializable)APagingFragment.this.getAdapter().getDatas().get(APagingFragment.this.getAdapter().getDatas().size() - 1));
                    } else {
                        APagingFragment.this.mPaging.processData(result, (Serializable)null, (Serializable)null);
                    }
                }

                if(this.mode == APagingFragment.RefreshMode.reset) {
                    APagingFragment.this.refreshConfig.pagingEnd = false;
                }

                if(this.mode == APagingFragment.RefreshMode.update || this.mode == APagingFragment.RefreshMode.reset) {
                    APagingFragment.this.refreshConfig.pagingEnd = ((List)resultList).size() == 0;
                }

                if(result instanceof IResult) {
                    IResult iResult = (IResult)result;
                    if(iResult.fromCache() && !iResult.outofdate()) {
                        APagingFragment.this.toLastReadPosition();
                    }

                    if(iResult.endPaging()) {
                        APagingFragment.this.refreshConfig.pagingEnd = true;
                    }
                }

                if(this.mode == APagingFragment.RefreshMode.reset && APagingFragment.this.getTaskCount(this.getTaskId()) > 1) {
                    APagingFragment.this.getAdapter().notifyDataSetChanged();
                }

                APagingFragment.this.setupRefreshViewWithConfig(APagingFragment.this.refreshConfig);
                Logger.d("AFragment-Paging", this.toString() + "-" + ABaseTaskState.success + " - " + this.mode);
                APagingFragment.this.onTaskStateChanged(ABaseTaskState.success, (TaskException)null, this.mode);
                super.onSuccess(result);
            } else {
                super.onSuccess(result);
            }
        }

        protected void onFailure(TaskException exception) {
            super.onFailure(exception);
            Logger.d("AFragment-Paging", this.toString() + "-" + ABaseTaskState.falid + " - " + this.mode + "-" + exception.getMessage());
            APagingFragment.this.onTaskStateChanged(ABaseTaskState.falid, exception, this.mode);
        }

        protected void onCancelled() {
            super.onCancelled();
            Logger.d("AFragment-Paging", this.toString() + "-" + ABaseTaskState.canceled + " - " + this.mode);
            APagingFragment.this.onTaskStateChanged(ABaseTaskState.canceled, (TaskException)null, this.mode);
        }

        protected void onFinished() {
            super.onFinished();
            Logger.d("AFragment-Paging", this.toString() + "-" + ABaseTaskState.finished + " - " + this.mode);
            APagingFragment.this.onTaskStateChanged(ABaseTaskState.finished, (TaskException)null, this.mode);
            APagingFragment.this.pagingTask = null;
        }

        protected boolean handleResult(APagingFragment.RefreshMode mode, List<T> datas) {
            return false;
        }

        protected abstract List<T> parseResult(Result var1);

        protected abstract Result workInBackground(APagingFragment.RefreshMode var1, String var2, String var3, Params... var4) throws TaskException;
    }

    public static class RefreshConfig implements Serializable {
        private static final long serialVersionUID = 6244426943442129360L;
        public boolean pagingEnd = false;
        public String positionKey = null;
        public boolean displayWhenScrolling = true;
        public int releaseDelay = 5000;
        public int[] releaseItemIds = null;
        public String emptyHint = "数据为空";
        public boolean footerMoreEnable = true;

        public RefreshConfig() {
        }
    }

    public static enum RefreshMode {
        reset,
        update,
        refresh;

        private RefreshMode() {
        }
    }
}
