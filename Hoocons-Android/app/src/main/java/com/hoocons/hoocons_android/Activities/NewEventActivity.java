package com.hoocons.hoocons_android.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.Adapters.ImageLoaderAdapter;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.EventBus.FriendModeRequest;
import com.hoocons.hoocons_android.EventBus.LoadedGifUriRequest;
import com.hoocons.hoocons_android.EventBus.PrivateModeRequest;
import com.hoocons.hoocons_android.EventBus.PublicModeRequest;
import com.hoocons.hoocons_android.EventBus.WarningContentRequest;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.LoadPreviewGifTask;
import com.hoocons.hoocons_android.ViewFragments.EventModeSheetFragment;

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
    @BindView(R.id.display_name)
    TextView mDisplayName;
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

    @BindView(R.id.new_event_text_content)
    EditText mTextContentInput;
    @BindView(R.id.post_event_profile)
    ImageView mProfileImage;
    @BindView(R.id.event_privacy)
    BootstrapButton mPrivacyBtn;
    @BindView(R.id.event_location)
    BootstrapButton mEventLocation;
    @BindView(R.id.event_warning)
    BootstrapButton mWarningButton;

    // Single Content view
    @BindView(R.id.new_event_single_content)
    RelativeLayout mSingleContentView;
    @BindView(R.id.event_single_content)
    AdjustableImageView mSingleContentImage;
    @BindView(R.id.delete_single_content)
    ImageButton mDeleteSingleContent;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;

    // Multiple images View
    @BindView(R.id.new_event_images_list)
    RecyclerView mImagesRecycler;

    private ArrayList<String> imagePaths;
    private ImageLoaderAdapter mImagesAdapter;
    private final int PHOTO_PICKER = 1;

    private String mMode = "Public";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);

        imagePaths = new ArrayList<>();

        initView();
    }

    private void initView() {
        mBack.setOnClickListener(this);
        mAddPhotoBtn.setOnClickListener(this);
        mAddGifBtn.setOnClickListener(this);

        mPrivacyBtn.setOnClickListener(this);
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

    private void showMode() {
        EventModeSheetFragment fragment = new EventModeSheetFragment();
        fragment.show(getSupportFragmentManager(), null);
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
        imagePaths.clear();
        imagePaths.addAll(imageList);
        mImagesAdapter = new ImageLoaderAdapter(this, imageList);
        mImagesRecycler.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        mImagesRecycler.setAdapter(mImagesAdapter);
        mImagesRecycler.setItemAnimator(new DefaultItemAnimator());
        mImagesRecycler.setNestedScrollingEnabled(false);
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
            case R.id.event_privacy:
                showMode();
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

    private void loadGif(String url) {
        Glide.with(this)
                .load(url)
                .asGif()
                .fitCenter()
                .crossFade()
                .listener(new RequestListener<String, GifDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mLoadingProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mSingleContentImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Giphy.REQUEST_GIPHY) {
            if (resultCode == Activity.RESULT_OK) {
                String downloadUrl = data.getStringExtra(GiphyActivity.GIF_DOWNLOAD_URL);
                loadGif(downloadUrl);
                updateUIforGifEvent();
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



    /**********************************************
     * EVENTBUS CATCHING FIELDS
     *
     *  + PublicModeRequest: Request Public Mode
     *
     *  +
     ***********************************************/
    @Subscribe
    public void onEvent(PublicModeRequest request) {
        mMode = "Public";
        String text = String.format("{%s}  %s  {%s}", FontAwesome.FA_GLOBE, mMode, FontAwesome.FA_CARET_DOWN);
        mPrivacyBtn.setText(text);
    }

    @Subscribe
    public void onEvent(PrivateModeRequest request) {
        mMode = "Private";
        String text = String.format("{%s}  %s  {%s}", FontAwesome.FA_USER, mMode, FontAwesome.FA_CARET_DOWN);
        mPrivacyBtn.setText(text);
    }

    @Subscribe
    public void onEvent(FriendModeRequest request) {
        mMode = "Friend";
        String text = String.format("{%s}  %s  {%s}", FontAwesome.FA_USERS, mMode, FontAwesome.FA_CARET_DOWN);
        mPrivacyBtn.setText(text);
    }

    @Subscribe
    public void onEvnet(WarningContentRequest request) {
        if (request.isRequested()) {
            mWarningButton.setVisibility(View.VISIBLE);
        } else {
            mWarningButton.setVisibility(View.GONE);
        }
    }
}
