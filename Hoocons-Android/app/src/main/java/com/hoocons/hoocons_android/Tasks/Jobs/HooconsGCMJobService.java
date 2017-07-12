package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.hoocons.hoocons_android.Managers.BaseApplication;

/**
 * Created by hungnguyen on 7/12/17.
 */

public class HooconsGCMJobService extends GcmJobSchedulerService {
    @NonNull
    @Override
    protected JobManager getJobManager() {
        return BaseApplication.getInstance().getJobManager();
    }
}