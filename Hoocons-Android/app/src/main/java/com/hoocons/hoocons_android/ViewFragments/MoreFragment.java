package com.hoocons.hoocons_android.ViewFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hoocons.hoocons_android.Activities.ChannelActivity;
import com.hoocons.hoocons_android.Activities.ChatActivity;
import com.hoocons.hoocons_android.Activities.FindMatchActivity;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.EventBus.UserNotFoundError;
import com.hoocons.hoocons_android.Helpers.ChatUtils;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.profile_header)
    ImageView mUserProfile;
    @BindView(R.id.find_love)
    TextView mFindLove;
    @BindView(R.id.help_center)
    TextView mHelpCenter;
    @BindView(R.id.display_name)
    TextView mDisplayName;
    @BindView(R.id.nick_name)
    TextView mNickname;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        initView();
        initTypeFaces();
        initClickListener();
    }

    private void initTypeFaces() {
        Context context = getContext();

        // Info name
        mDisplayName.setTypeface(EasyFonts.robotoBold(context));
        mNickname.setTypeface(EasyFonts.robotoRegular(context));
    }

    private void initClickListener() {
        mUserProfile.setOnClickListener(this);
        mFindLove.setOnClickListener(this);
        mHelpCenter.setOnClickListener(this);
    }

    private void initView() {
        loadProfileImage();

        mDisplayName.setText(SharedPreferencesManager.getDefault().getUserDisplayName());
        String nickname = "@" + SharedPreferencesManager.getDefault().getUserNickname();
        mNickname.setText(nickname);
    }

    private void loadProfileImage() {
        Glide.with(this)
                .load(SharedPreferencesManager.getDefault().getUserProfileUrl())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ab_progress))
                .apply(RequestOptions.errorOf(R.drawable.image_holder))
                .into(mUserProfile);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void startFindMatchActivity() {
        Intent findMatchIntent = new Intent(getActivity(), FindMatchActivity.class);
        startActivity(findMatchIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_header:

                break;
            case R.id.find_love:
                startFindMatchActivity();
                break;
            case R.id.help_center:
                startActivity(new Intent(getActivity(), ChannelActivity.class));
                break;
            default:
                break;
        }
    }
}
