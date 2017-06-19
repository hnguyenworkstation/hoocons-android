package com.hoocons.hoocons_android.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.EventBus.LoadedGifUriRequest;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.LoadPreviewGifTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;
import xyz.klinker.giphy.Giphy;
import xyz.klinker.giphy.GiphyActivity;

public class NewEventActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.action_back)
    ImageButton mBack;
    @BindView(R.id.action_post)
    Button mPostBtn;
    @BindView(R.id.post_event_profile)
    ImageView mProfileImage;
    @BindView(R.id.display_name)
    TextView mDisplayName;
    @BindView(R.id.event_privacy)
    BootstrapButton mPrivacyBtn;
    @BindView(R.id.event_location)
    BootstrapButton mLocationBtn;
    @BindView(R.id.event_add_photo)
    ImageView mAddPhotoBtn;
    @BindView(R.id.event_add_video)
    ImageView mAddVideoBtn;
    @BindView(R.id.event_add_location)
    ImageView mAddLocationBtn;
    @BindView(R.id.event_add_sound)
    ImageView mAddSoundBtn;
    @BindView(R.id.event_add_gif)
    ImageView mAddGifBtn;

    // Single Content view
    @BindView(R.id.new_event_single_content)
    RelativeLayout mSingleContentView;
    @BindView(R.id.event_single_content)
    AdjustableImageView mSingleContentImage;
    @BindView(R.id.delete_single_content)
    ImageButton mDeleteSingleContent;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;


    private final int PHOTO_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mBack.setOnClickListener(this);
        mAddPhotoBtn.setOnClickListener(this);
        mAddGifBtn.setOnClickListener(this);
    }

    private void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_title))
                .setMessage(getString(R.string.delete_warning))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishActivity();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void finishActivity() {
        this.finish();
        overridePendingTransition(R.anim.fix_anim, R.anim.slide_down_out);
    }

    @Override
    public void onBackPressed(){
        if (true) {
            showAlert();
        } else {
            finishActivity();
        }
    }

    private void loadPickedImage(ArrayList<String> imageList) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_add_photo:
                AppUtils.startImagePicker(this, 20, PHOTO_PICKER);
                break;
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.event_add_gif:
                new Giphy.Builder(NewEventActivity.this, AppConstant.GIPHY_PUBLIC_KEY)// their public BETA key
                        .maxFileSize(2 * 1024 * 1024) //2 mb
                        .start();
                break;
            default:
                break;
        }
    }

    private void updateUIforGifEvent() {
        // display the holder content
        mSingleContentView.setVisibility(View.VISIBLE);
        mDeleteSingleContent.bringToFront();

        // update the add more content options
        mAddPhotoBtn.setVisibility(View.GONE);
        mAddLocationBtn.setVisibility(View.GONE);
        mAddVideoBtn.setVisibility(View.GONE);
        mAddSoundBtn.setVisibility(View.GONE);
    }

    private void updateUIforNormalEvent() {
        mAddPhotoBtn.setVisibility(View.VISIBLE);
        mAddLocationBtn.setVisibility(View.VISIBLE);
        mAddVideoBtn.setVisibility(View.VISIBLE);
        mAddSoundBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Giphy.REQUEST_GIPHY) {
            if (resultCode == Activity.RESULT_OK) {
                String downloadUrl = data.getStringExtra(GiphyActivity.GIF_DOWNLOAD_URL);

                updateUIforGifEvent();
                new LoadPreviewGifTask(this, downloadUrl).execute();
            }
        } else if (requestCode == PHOTO_PICKER) {
            if (data != null){
                final ArrayList<String> images = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                if (images.size() >= 1) {
                    loadPickedImage(images);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe
    public void onEvent(LoadedGifUriRequest request) {
        Glide.with(this)
                .load(request.getGifUri())
                .asGif()
                .fitCenter()
                .crossFade()
                .listener(new RequestListener<Uri, GifDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model,
                                               Target<GifDrawable> target,
                                               boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Uri model,
                                                   Target<GifDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        mLoadingProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mSingleContentImage);
    }
}
