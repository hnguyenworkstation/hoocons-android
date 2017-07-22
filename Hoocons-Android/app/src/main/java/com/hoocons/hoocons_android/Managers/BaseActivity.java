package com.hoocons.hoocons_android.Managers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.GoogleApiAvailability;
import org.aisen.android.component.bitmaploader.core.BitmapOwner;
import org.aisen.android.network.task.ITaskManager;
import org.aisen.android.network.task.TaskManager;
import org.aisen.android.network.task.WorkTask;
import org.aisen.android.ui.widget.AsToolbar;

import java.util.Locale;

/**
 * Created by hungnguyen on 6/3/17.
 */
public class BaseActivity extends AppCompatActivity implements BitmapOwner, ITaskManager, AsToolbar.OnToolbarDoubleClick{
    public static final String TAG = BaseActivity.class.getSimpleName();
    private static BaseActivity mInstance;
    private static BaseActivity runningActivity;
    private TaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        this.taskManager = new TaskManager();

        if(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == 0){
            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
        }
    }

    @Override
    public int getTaskCount(String taskId) {
        return this.taskManager.getTaskCount(taskId);
    }


    public final void addTask(WorkTask task) {
        this.taskManager.addTask(task);
    }

    public final void removeTask(String taskId, boolean cancelIfRunning) {
        this.taskManager.removeTask(taskId, cancelIfRunning);
    }

    public final void removeAllTask(boolean cancelIfRunning) {
        this.taskManager.removeAllTask(cancelIfRunning);
    }

    @Override
    public boolean canDisplay() {
        return false;
    }

    @Override
    public boolean onToolbarDoubleClick() {
        return false;
    }

    public static BaseActivity getRunningActivity() {
        return runningActivity;
    }

    public static void setRunningActivity(BaseActivity activity) {
        runningActivity = activity;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setRunningActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
