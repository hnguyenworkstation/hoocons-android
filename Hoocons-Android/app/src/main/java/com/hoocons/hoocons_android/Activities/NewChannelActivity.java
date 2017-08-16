package com.hoocons.hoocons_android.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.ChannelCategoryCollected;
import com.hoocons.hoocons_android.EventBus.ChannelDescCollected;
import com.hoocons.hoocons_android.EventBus.ChannelImageCropCollected;
import com.hoocons.hoocons_android.EventBus.ChannelNameCollected;
import com.hoocons.hoocons_android.EventBus.TagsCollected;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.EventBus.UploadImageFailed;
import com.hoocons.hoocons_android.EventBus.UploadImageSuccess;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.Requests.ChannelRequest;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.NewChannelJob;
import com.hoocons.hoocons_android.Tasks.Jobs.UploadSingleUriImageJob;
import com.hoocons.hoocons_android.ViewFragments.GetChannelAboutFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelCategoryFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelNameFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelProfileFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelTagsFragment;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;

public class NewChannelActivity extends BaseActivity {
    @BindView(R.id.create_channel_container)
    FrameLayout mFrameContainer;
    @BindView(R.id.action_back)
    ImageButton mActionBack;
    @BindView(R.id.act_title)
    TextView mTitle;
    @BindView(R.id.action_skip)
    Button mActionSkip;
    @BindView(R.id.wallpaper)
    ImageView mWallpaper;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.edit_wallpaper)
    ImageView mEditWallpaper;
    @BindView(R.id.wallpaper_root)
    RelativeLayout mWallpaperRoot;

    private FragmentManager mFragManager;
    private FragmentTransaction mFragTransition;
    private GetChannelNameFragment getChannelNameFragment;
    private GetChannelAboutFragment getChannelAboutFragment;
    private GetChannelCategoryFragment getChannelCategoryFragment;
    private GetChannelTagsFragment getChannelTagsFragment;
    private GetChannelProfileFragment getChannelProfileFragment;
    private final String TAG = NewChannelActivity.class.getSimpleName();

    private final int WALLPAPER_IMG_PICKER = 10;
    private static final String WALLPAPER_CROPPED_IMAGE_NAME = "ProfileCroppedImage";
    private String wallpaperImagePath;
    private Uri wallpaperCroppedUri;
    private Uri profileCroppedUri;
    private String profileUrl;
    private String wallpaperUrl;

    private String channelName;
    private String channelCat;
    private String channelAbout;
    private List<String> topics;

    private final String UPLOAD_PROFILE_TAG = "upload_profile";
    private final String UPLOAD_WALLPAPER_TAG = "upload_wallpaper";
    private MaterialDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_new_channel);
        ButterKnife.bind(this);

        topics = new ArrayList<>();

        getChannelNameFragment = new GetChannelNameFragment();
        getChannelAboutFragment = new GetChannelAboutFragment();
        getChannelCategoryFragment = new GetChannelCategoryFragment();

        initGeneralView();
        initGetNameView();
    }

    private void initGeneralView() {
        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();
        AppUtils.loadCropImageWithProgressBar(this, "http://www.stanleychowillustration.com/uploads/images/MANCHESTER_LANDSCAPE_30x12.jpg", mWallpaper, mProgressBar);

        mActionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initGetNameView() {
        mFragTransition.replace(R.id.create_channel_container, getChannelNameFragment, "get_channel_name");
        mFragTransition.commit();

        mActionSkip.setVisibility(View.GONE);
        mEditWallpaper.setVisibility(View.GONE);
        mActionBack.setImageResource(R.drawable.ic_close_colored);
    }

    private void initGetChannelAboutView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelAboutFragment, "get_channel_about");
        mFragTransition.commit();

        mActionSkip.setVisibility(View.GONE);
        mEditWallpaper.setVisibility(View.GONE);
        mActionBack.setImageResource(R.drawable.ic_arrow_back_colored);
    }

    private void initGetCategoryView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelCategoryFragment, "get_channel_category");
        mFragTransition.commit();
        mEditWallpaper.setVisibility(View.GONE);
    }

    private void initGetProfileView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelProfileFragment, "get_channel_profile");
        mFragTransition.commit();

        mWallpaperRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragManager.findFragmentByTag("get_channel_profile") != null) {
                    AppUtils.startImagePicker(NewChannelActivity.this, 1, WALLPAPER_IMG_PICKER);
                }
            }
        });

        mEditWallpaper.setVisibility(View.VISIBLE);
    }

    private void initGetTagsView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelTagsFragment, "get_channel_tags");
        mFragTransition.commit();
        mEditWallpaper.setVisibility(View.GONE);
    }

    private void hideKeyboard(final Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed(){
        if (mFragManager.findFragmentByTag("get_channel_about") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, getChannelNameFragment, "get_channel_name");
            mFragTransition.commit();
        } else if (mFragManager.findFragmentByTag("get_channel_category") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, getChannelAboutFragment, "get_channel_about");
            mFragTransition.commit();
        } else if (mFragManager.findFragmentByTag("get_channel_profile") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, getChannelTagsFragment, "get_channel_tags");
            mFragTransition.commit();
        } else if (mFragManager.findFragmentByTag("get_channel_tags") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, getChannelCategoryFragment, "get_channel_category");
            mFragTransition.commit();
        } else if (mFragManager.getBackStackEntryCount() > 0) {
            Log.i(TAG, "popping backstack");
            mFragManager.popBackStack();
            finish();
            overridePendingTransition(R.anim.fade_in_from_left, R.anim.fade_out_to_right);
        } else {
            Log.i(TAG, "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WALLPAPER_IMG_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null){
                    final ArrayList<String> images = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                    if (images.size() >= 1) {
                        wallpaperImagePath = images.get(0);
                        AppUtils.startWallpaperImageCropActivity(NewChannelActivity.this, Uri.fromFile(new File(images.get(0)))
                                , WALLPAPER_CROPPED_IMAGE_NAME);
                    }
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            } else {
                handleCropResult(data);
            }
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // Load uri from here
            wallpaperCroppedUri = resultUri;
            AppUtils.loadCropSquareImageFromUri(this, resultUri, mWallpaper, null);
        } else {
            Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void showUploadingDialog() {
        loadingDialog = new MaterialDialog.Builder(this)
                .title(R.string.creating_channel)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .cancelable(false)
                .show();
    }

    private void startUploadingChannelProfile() {
        showUploadingDialog();

        if (profileCroppedUri != null && profileUrl == null) {
            BaseApplication.getInstance().getJobManager()
                    .addJobInBackground(new UploadSingleUriImageJob(profileCroppedUri,
                            UPLOAD_PROFILE_TAG, AppConstant.CHANNEL_PATH));
        } else if (wallpaperCroppedUri != null && wallpaperUrl == null) {
            BaseApplication.getInstance().getJobManager()
                    .addJobInBackground(new UploadSingleUriImageJob(wallpaperCroppedUri,
                            UPLOAD_WALLPAPER_TAG, AppConstant.CHANNEL_PATH));
        }
    }

    private void cancelDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void submitNewChannel() {
        ChannelRequest request = new ChannelRequest(channelName, "subname", channelAbout,
                profileUrl, topics, "Public", wallpaperUrl);
        BaseApplication.getInstance().getJobManager().addJobInBackground(new NewChannelJob(request));
    }

    @Subscribe
    public void onEvent(ChannelNameCollected event) {
        channelName = event.getName();
        initGetChannelAboutView();
        hideKeyboard(this);
    }

    @Subscribe
    public void onEvent(ChannelDescCollected desc) {
        channelAbout = desc.getDesc();
        initGetCategoryView();
        hideKeyboard(this);
    }

    @Subscribe
    public void onEvent(ChannelCategoryCollected cat) {
        channelCat = cat.getCategory();
        getChannelTagsFragment = GetChannelTagsFragment.newInstance(channelCat);
        initGetTagsView();
        hideKeyboard(this);
    }

    @Subscribe
    public void onEvent(TagsCollected ev) {
        getChannelProfileFragment = GetChannelProfileFragment.newInstance(channelName, channelCat,
                (ArrayList<String>) ev.getTags());
        initGetProfileView();
    }

    @Subscribe
    public void onEvent(ChannelImageCropCollected ev) {
        profileCroppedUri = ev.getUri();

        startUploadingChannelProfile();
    }

    @Subscribe
    public void onEvent(UploadImageSuccess job) {
        /*
        * Scenarios:
        * 1. If uploaded channel profile => check if channel wallpaper need to uploaded or not
        *  => upload wallpaper if needed
        *
        * 2. If wallpaper is uploaded (means that profile already uploaded or null)
        *  => submit new channel to server
        * */

        if (job.getTag().equals(UPLOAD_PROFILE_TAG)) {
            profileUrl = job.getUrl();

            if (wallpaperCroppedUri != null && wallpaperUrl == null) {
                BaseApplication.getInstance().getJobManager()
                        .addJobInBackground(new UploadSingleUriImageJob(wallpaperCroppedUri,
                                UPLOAD_WALLPAPER_TAG, AppConstant.CHANNEL_PATH));
            } else {
                submitNewChannel();
            }
        } else if (job.getTag().equals(UPLOAD_WALLPAPER_TAG)) {
            wallpaperUrl = job.getUrl();

            submitNewChannel();
        }
    }

    @Subscribe
    public void onEvent(UploadImageFailed job) {
        cancelDialog();
        Toast.makeText(this, getResources().getString(R.string.task_failed), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(TaskCompleteRequest task) {
        if (task.getTag().equals(AppConstant.CREATE_NEW_CHANNEL)) {
            cancelDialog();
            Toast.makeText(this, getResources().getString(R.string.created_channel), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onEvent(BadRequest request) {
        cancelDialog();
        Toast.makeText(this, getResources().getString(R.string.task_failed), Toast.LENGTH_SHORT).show();
    }
}
