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
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.LoadPreviewGifTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;
import xyz.klinker.giphy.Giphy;
import xyz.klinker.giphy.GiphyActivity;
import xyz.klinker.giphy.GiphyApiHelper;

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

    private final int PHOTO_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        FragmentManager fm = getFragmentManager();
                        if (fm.getBackStackEntryCount() > 0) {
                            Log.i(TAG, "popping backstack");
                            fm.popBackStack();
                            finish();
                            overridePendingTransition(R.anim.fade_in_from_left, R.anim.fade_out_to_right);
                        } else {
                            Log.i(TAG, "nothing on backstack, calling super");
                        }
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

    @Override
    public void onBackPressed(){
        if (true) {
            showAlert();
        } else {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i(TAG, "popping backstack");
                fm.popBackStack();
                finish();
                overridePendingTransition(R.anim.fade_in_from_left, R.anim.fade_out_to_right);
            } else {
                Log.i(TAG, "nothing on backstack, calling super");
                super.onBackPressed();
            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Giphy.REQUEST_GIPHY) {
            if (resultCode == Activity.RESULT_OK) {
                String downloadUrl = data.getStringExtra(GiphyActivity.GIF_DOWNLOAD_URL);

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
}
