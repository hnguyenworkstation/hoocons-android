package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.hoocons.hoocons_android.CustomUI.CustomFlowLayout;
import com.hoocons.hoocons_android.EventBus.TagsCollected;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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

    private List<String> hobbies;

    private String mCategory;

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
        hobbies = new ArrayList<>();
        hobbies.add(mCategory);
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

}