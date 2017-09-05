package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.hoocons.hoocons_android.CustomUI.CustomFlowLayout;
import com.hoocons.hoocons_android.EventBus.TagsCollected;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectUserHobbiesFragment extends Fragment {
    @BindView(R.id.topic_flow_layout)
    CustomFlowLayout mFlowLayout;
    @BindView(R.id.add_topic_btn)
    BootstrapButton mAddTopic;
    @BindView(R.id.topic_input)
    EditText mInput;
    @BindView(R.id.gcn_next)
    Button mNext;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.reason)
    TextView mReason;
    @BindView(R.id.example_hobbies)
    TextView mSampleHobbies;

    private MaterialDialog mFlowExampleDialog;
    private List<String> hobbies;
    private List<String> exampleHobbies;
    private List<String> pickedExampleHobbies;

    public CollectUserHobbiesFragment() {
        // Required empty public constructor
    }

    public static CollectUserHobbiesFragment newInstance(String param1, String param2) {
        CollectUserHobbiesFragment fragment = new CollectUserHobbiesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exampleHobbies = new ArrayList<>();
        pickedExampleHobbies = new ArrayList<>();

        String[] temp = getResources().getStringArray(R.array.example_english_hobbies);
        exampleHobbies.addAll(Arrays.asList(temp));

        hobbies = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collect_user_hobbies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initFlowLayoutView();

        initCustomDialog();
        initTextAndTypeFace();

        mAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTopicView();
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new TagsCollected(hobbies));
            }
        });

        mSampleHobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExampleHobbiesDialog();
            }
        });
    }

    private void initTextAndTypeFace() {
        mSampleHobbies.setText(getResources().getString(R.string.pick_sample_hobbies));
        mSampleHobbies.setTypeface(EasyFonts.robotoBold(getContext()));

        mTitle.setText(getResources().getString(R.string.pick_hobbies));
        mReason.setText(getResources().getString(R.string.pick_hobbies_desc));

        mReason.setTypeface(EasyFonts.robotoRegular(getContext()));
        mTitle.setTypeface(EasyFonts.robotoBold(getContext()));

        mInput.setTypeface(EasyFonts.robotoRegular(getContext()));
    }

    private void initCustomDialog() {
        mFlowExampleDialog = new MaterialDialog.Builder(getContext())
                .title(R.string.example_hobbies)
                .customView(R.layout.empty_flow_layout, true)
                .positiveText(getResources().getString(R.string.pick))
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .negativeColor(getResources().getColor(R.color.gray_alpha))
                .negativeText(getResources().getString(R.string.cancel))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();
    }

    private void initCustomDialogLayout() {
        View view = mFlowExampleDialog.getCustomView();
        CustomFlowLayout flowLayout = (CustomFlowLayout) view.findViewById(R.id.custom_flow_layout);

        for (int i = 0; i < exampleHobbies.size(); i++) {
            final RelativeLayout item = (RelativeLayout) getLayoutInflater()
                    .inflate(R.layout.category_flow_item_layout,
                            mFlowLayout, false);
            TextView topic = (TextView) item.findViewById(R.id.topic_flow_text);

            topic.setText(exampleHobbies.get(i));
            topic.setTypeface(EasyFonts.robotoRegular(getContext()));
            item.setTag(i);

            flowLayout.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag();
                    String hobby = exampleHobbies.get(i);

                    if (pickedExampleHobbies.contains(hobby)) {
                        pickedExampleHobbies.remove(hobby);
                        item.setBackground(getResources().getDrawable(R.drawable.rounded_corner_frame));
                    } else {
                        item.setBackground(getResources().getDrawable(R.drawable.active_button));
                        pickedExampleHobbies.add(hobby);
                    }
                }
            });
        }
    }

    private void showExampleHobbiesDialog() {
        pickedExampleHobbies.clear();
        initCustomDialogLayout();
        mFlowExampleDialog.show();
    }

    private void addTopicView() {
        if (mInput.getText().length() > 0) {
            String topic = mInput.getText().toString();

            if (hobbies.contains(topic)) {
                Toast.makeText(getContext(), getResources().getText(R.string.already_created),
                        Toast.LENGTH_SHORT).show();
            } else {
                hobbies.add(topic);
                initFlowLayoutView();
            }

            mInput.setText("");
        }
    }

    private void initFlowLayoutView() {
        mFlowLayout.removeAllViews();

        if (hobbies.size() == 0) {
            mFlowLayout.setVisibility(View.GONE);
        } else {
            mFlowLayout.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < hobbies.size(); i++) {
            final RelativeLayout item = (RelativeLayout) getLayoutInflater().inflate(R.layout.category_flow_item_layout,
                    mFlowLayout, false);
            TextView topic = (TextView) item.findViewById(R.id.topic_flow_text);

            topic.setText(hobbies.get(i));
            topic.setTypeface(EasyFonts.robotoRegular(getContext()));
            item.setTag(i);

            mFlowLayout.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag();
                    item.setVisibility(View.GONE);
                    updateTags(i);
                    hobbies.remove(i);

                    if (hobbies.size() == 0) {
                        mFlowLayout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void updateTags(int i) {
        for (i = i + 1; i < hobbies.size(); i++) {
            RelativeLayout flowChild = (RelativeLayout) mFlowLayout.getChildAt(i);
            int temp = (int) flowChild.getTag();
            flowChild.setTag(temp - 1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFlowExampleDialog.dismiss();
    }
}
