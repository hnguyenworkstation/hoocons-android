package com.hoocons.hoocons_android.ViewFragments;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.CompleteLoginRequest;
import com.hoocons.hoocons_android.EventBus.FieldAvailableRequest;
import com.hoocons.hoocons_android.EventBus.FieldUnavailableRequest;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.EventBus.UploadImageFailed;
import com.hoocons.hoocons_android.EventBus.UploadImageSuccess;
import com.hoocons.hoocons_android.EventBus.UserBasicInfoCollected;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.CheckNicknameAvailabilityTask;
import com.hoocons.hoocons_android.Tasks.Jobs.UpdateUserInfoJob;
import com.hoocons.hoocons_android.Tasks.Jobs.UploadSingleUriImageJob;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.iwf.photopicker.PhotoPicker;

public class NewUserInfoFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener{
    @BindView(R.id.profile_image)
    ImageView mProfileImgView;
    @BindView(R.id.display_name_input)
    EditText mDisplayNameInput;
    @BindView(R.id.birthday_txt)
    TextView mBirthDayText;
    @BindView(R.id.gender_male)
    RadioButton mGenderMale;
    @BindView(R.id.gender_female)
    RadioButton mGenderFemale;
    @BindView(R.id.submit_button)
    Button mSubmitBtn;
    @BindView(R.id.details_txt)
    TextView mDetailsText;

    private final String TAG = NewUserInfoFragment.class.getSimpleName();
    private String profileUrl;

    public static final int PHOTO_PICKER = 5;

    private String mParam1;
    private String mParam2;

    private int mBirthYear;
    private String mBirthTime;
    private String mProfileImagePath;

    private DatePickerDialog datePickerDialog;
    private MaterialDialog mUploadingDialog;

    private FragmentTransaction mFragTransition;
    private FragmentManager mFragManager;

    public NewUserInfoFragment() {

    }

    public static NewUserInfoFragment newInstance(String param1, String param2) {
        NewUserInfoFragment fragment = new NewUserInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_user_info, container, false);
        ButterKnife.bind(this, rootView);

        mBirthDayText.setOnClickListener(this);
        mProfileImgView.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);

        mUploadingDialog = new MaterialDialog.Builder(getContext())
                .title(getResources().getString(R.string.uploading_profile))
                .content(getResources().getString(R.string.please_wait))
                .progress(true, 0)
                .cancelable(false)
                .build();


        mFragManager = getActivity().getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();

        return rootView;
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                getContext(), this, now.get(Calendar.YEAR),
                now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private boolean validateBirthday() {
        if ((Calendar.getInstance().get(Calendar.YEAR) - mBirthYear) < 7) {
            mBirthDayText.setError(getResources().getString(R.string.not_enough_ages));
            return false;
        }
        return true;
    }

    private boolean validateNameField() {
        String name = mDisplayNameInput.getText().toString();
        if (name.isEmpty()) {
            mDisplayNameInput.setError(getResources().getString(R.string.error_empty_name));
            return false;
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showWarningEmptyProfileDialog() {
        new MaterialDialog.Builder(getContext())
                .title(getResources().getString(R.string.profile_required))
                .content(getResources().getString(R.string.error_empty_profile))
                .icon(getResources().getDrawable(R.drawable.warning_circle))
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .cancelable(true)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                })
                .build().show();
    }

    private boolean validateProfileView() {
        if (mProfileImagePath != null && !mProfileImagePath.isEmpty()) {
            return true;
        } else {
            showWarningEmptyProfileDialog();
            return false;
        }
    }

    private void showUploadingProfileDialog() {
        mUploadingDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthday_txt:
                showDatePickerDialog();
                break;
            case R.id.profile_image:
                AppUtils.startImagePickerFromFragment(getContext(), this, 1, PHOTO_PICKER);
                break;
            case R.id.submit_button:
                if (validateNameField() && validateBirthday() && validateProfileView()) {
                    showUploadingProfileDialog();
                    BaseApplication.getInstance().getJobManager()
                            .addJobInBackground(
                                    new UploadSingleUriImageJob(mProfileImagePath, null,
                                                AppConstant.PROFILE_PATH));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.mBirthTime = String.format("%s-%s-%s", year, month + 1, dayOfMonth);
        this.mBirthYear = year;
        mBirthDayText.setText(String.format("%s/%s/%s", dayOfMonth, month + 1, year));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            final ArrayList<String> images = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

            if (images.size() >= 1) {
                loadProfileImage(images.get(0));
            }
        }
    }

    private void loadProfileImage(String image) {
        mProfileImagePath = image;

        Glide.with(getContext())
                .load(image)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ab_progress))
                .apply(RequestOptions.errorOf(R.drawable.image_holder))
                .into(mProfileImgView);
    }

    private void submitData() {
        EventBus.getDefault().post(new UserBasicInfoCollected(profileUrl,
                mDisplayNameInput.getText().toString(), mGenderMale.isSelected()?"Male":"Female",
                mBirthTime));
        transferToCollectNicknameStage();
    }

    private void transferToCollectNicknameStage() {
        mFragTransition.replace(R.id.info_fragment_container, new CollectNicknameFragment());
        mFragTransition.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    /**********************************************
     * EVENTBUS CATCHING FIELDS
     *  + FieldAvailableRequest
     *  + BadRequest
     ********************************************** */
    @Subscribe
    public void onEvent(UploadImageSuccess task) {
        mUploadingDialog.dismiss();
        profileUrl = task.getUrl();
        submitData();
    }

    @Subscribe
    public void onEvent(UploadImageFailed task) {
        mUploadingDialog.dismiss();
        Toast.makeText(getContext(), getResources().getString(R.string.upload_failed_tryagain),
                Toast.LENGTH_SHORT).show();
    }
}
