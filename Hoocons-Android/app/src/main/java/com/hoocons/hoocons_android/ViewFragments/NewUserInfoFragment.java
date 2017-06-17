package com.hoocons.hoocons_android.ViewFragments;


import android.app.DatePickerDialog;
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

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.FieldAvailableRequest;
import com.hoocons.hoocons_android.EventBus.FieldUnavailableRequest;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.CheckNicknameAvailabilityTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private String mParam1;
    private String mParam2;

    private int mBirthYear;
    private String mBirthTime;

    private DatePickerDialog datePickerDialog;

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

        initView();

        mCheckNicknameBtn.setOnClickListener(this);
        mBirthDayText.setOnClickListener(this);

        return rootView;
    }

    private void initView() {
        Calendar now = Calendar.getInstance();
        mBirthDayText.setText(String.format("%s/%s/%s", now.get(Calendar.MONTH) + 1,
                now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.YEAR)));
        mBirthDayText.setInputType(InputType.TYPE_NULL);
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
        if (nickname.matches("[a-zA-Z0-9]*")) {
            return true;
        } else {
            mNicknameInput.setError(getResources().getString(R.string.nickname_error));
            return false;
        }
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
            default:
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.mBirthTime = String.format("%s-%s-%s", year, month + 1, dayOfMonth);
        this.mBirthYear = year;
        mBirthDayText.setText(mBirthTime);
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
    public void onEvent(BadRequest request) {

    }
}
