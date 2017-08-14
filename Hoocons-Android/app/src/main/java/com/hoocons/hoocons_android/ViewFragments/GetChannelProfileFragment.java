package com.hoocons.hoocons_android.ViewFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;

public class GetChannelProfileFragment extends Fragment {
    @BindView(R.id.pick_image)
    TextView mPickImageTxt;
    @BindView(R.id.profile_view)
    ImageView mProfileImage;
    @BindView(R.id.profile_root)
    RelativeLayout mProfileRoot;
    @BindView(R.id.channel_name)
    TextView mChannelName;
    @BindView(R.id.gcn_next)
    Button mNextBtn;

    private static final String CHANNEL_NAME = "name";
    public static final int PHOTO_PICKER = 5;
    private static final String CROPPED_IMAGE_NAME = "CroppedImage";
    private final String TAG = GetChannelProfileFragment.class.getSimpleName();

    private String mName;
    private Uri profileCroppedUri;
    private String profileImagePath;


    public GetChannelProfileFragment() {
        // Required empty public constructor
    }

    public static GetChannelProfileFragment newInstance(String channelName) {
        GetChannelProfileFragment fragment = new GetChannelProfileFragment();
        Bundle args = new Bundle();
        args.putString(CHANNEL_NAME, channelName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(CHANNEL_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_channel_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initView();
        initClickListener();
    }

    private void initView() {
        mChannelName.setText(mName);
        AppUtils.loadCropImageWithProgressBar(getContext(),
                "http://www.stanleychowillustration.com/uploads/images/MANCHESTER_LANDSCAPE_30x12.jpg",
                mProfileImage, null);
    }

    private void initClickListener() {
        mPickImageTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.startImagePickerFromFragment(getContext(), GetChannelProfileFragment.this,
                        1, PHOTO_PICKER);
            }
        });

        mProfileRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.startImagePickerFromFragment(getContext(), GetChannelProfileFragment.this,
                        1, PHOTO_PICKER);
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_PICKER) {
            if (data != null){
                final ArrayList<String> images = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                if (images.size() >= 1) {
                    profileImagePath = images.get(0);
                    AppUtils.startCropActivity(getActivity(), Uri.fromFile(new File(images.get(0)))
                            , CROPPED_IMAGE_NAME);
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            } else if (resultCode == Activity.RESULT_OK) {
                handleCropResult(data);
            }
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // Load uri from here
            profileCroppedUri = resultUri;
            AppUtils.loadCropSquareImageFromUri(getContext(), profileCroppedUri, mProfileImage, null);
        } else {
            Toast.makeText(getContext(), R.string.toast_cannot_retrieve_cropped_image,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(getContext(), cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

}
