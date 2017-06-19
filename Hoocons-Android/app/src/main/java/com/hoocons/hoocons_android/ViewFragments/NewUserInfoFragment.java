package com.hoocons.hoocons_android.ViewFragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
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

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hoocons.hoocons_android.CustomUI.GlideCircleTransformation;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.CompleteLoginRequest;
import com.hoocons.hoocons_android.EventBus.FieldAvailableRequest;
import com.hoocons.hoocons_android.EventBus.FieldUnavailableRequest;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.CheckNicknameAvailabilityTask;
import com.hoocons.hoocons_android.Tasks.UpdateUserInfoTask;

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
    @BindView(R.id.nickname_input)
    EditText mNicknameInput;
    @BindView(R.id.check_nickname_btn)
    BootstrapButton mCheckNicknameBtn;
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
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;


    private final String TAG = NewUserInfoFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int PHOTO_PICKER = 5;

    private String mParam1;
    private String mParam2;

    private int mBirthYear;
    private String mBirthTime;
    private String mProfileImagePath;

    private DatePickerDialog datePickerDialog;
    private SweetAlertDialog pDialog;

    public NewUserInfoFragment() {

    }

    public static NewUserInfoFragment newInstance(String param1, String param2) {
        NewUserInfoFragment fragment = new NewUserInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_user_info, container, false);
        ButterKnife.bind(this, rootView);

        mCheckNicknameBtn.setOnClickListener(this);
        mBirthDayText.setOnClickListener(this);
        mProfileImgView.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);

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

    private boolean validateNicknameField() {
        String nickname = mNicknameInput.getText().toString();
        if (nickname.isEmpty()) {
            mNicknameInput.setError(getResources().getString(R.string.error_empty_nickname));
            return false;
        }
        if (nickname.matches("[a-zA-Z0-9]*") && nickname.length() >= 5) {
            return true;
        } else {
            mNicknameInput.setError(getResources().getString(R.string.nickname_error));
            return false;
        }
    }

    private boolean validateNameField() {
        String name = mDisplayNameInput.getText().toString();
        if (name.isEmpty()) {
            mDisplayNameInput.setError(getResources().getString(R.string.error_empty_name));
            return false;
        }

        return true;
    }

    private void showProcessDialog() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText(getResources().getString(R.string.updating));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_nickname_btn:
                if (validateNicknameField()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    new CheckNicknameAvailabilityTask(mNicknameInput.getText().toString()).execute();
                }
                break;
            case R.id.birthday_txt:
                showDatePickerDialog();
                break;
            case R.id.profile_image:
                AppUtils.startImagePickerFromFragment(getContext(), this, 1, PHOTO_PICKER);
                break;
            case R.id.submit_button:
                if (validateNameField() && validateNicknameField() && validateBirthday()) {
                    showProcessDialog();
                    new UpdateUserInfoTask(
                            mDisplayNameInput.getText().toString(),
                            mNicknameInput.getText().toString(),
                            mGenderFemale.isChecked() ?
                                    AppConstant.GENDER_FEMALE :
                                    AppConstant.GENDER_MALE,
                            mBirthTime,
                            mProfileImagePath)
                            .execute();
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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransformation(getContext()))
                .placeholder(R.drawable.ab_progress)
                .error(R.drawable.image_holder)
                .crossFade()
                .into(mProfileImgView);
    }

    private void completeLoginProcess() {
        pDialog.dismiss();
        EventBus.getDefault().post(new CompleteLoginRequest());
    }


    /**********************************************
     * EVENTBUS CATCHING FIELDS
     *  + FieldAvailableRequest
     *  + FieldUnavailableRequest
     *  + BadRequest
     ********************************************** */
    @Subscribe
    public void onEvent(FieldAvailableRequest request) {
        mProgressBar.setVisibility(View.GONE);
        mCheckNicknameBtn.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
        mCheckNicknameBtn.setBootstrapText(new BootstrapText.Builder(getContext())
                .addFontAwesomeIcon(FontAwesome.FA_CHECK_CIRCLE)
                .addText(" " + getContext().getResources().getText(R.string.checked))
                .build());
    }

    @Subscribe
    public void onEvent(FieldUnavailableRequest request) {
        mProgressBar.setVisibility(View.GONE);
        mNicknameInput.setError(getResources().getString(R.string.nickname_unavailable));
    }

    @Subscribe
    public void onEvent(TaskCompleteRequest request) {
        completeLoginProcess();
    }

    @Subscribe
    public void onEvent(BadRequest request) {
        pDialog.dismiss();
        Toast.makeText(getContext(), getResources().getString(R.string.bad_request), Toast.LENGTH_SHORT).show();
    }
}
