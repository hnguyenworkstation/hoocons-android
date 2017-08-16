package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.hoocons.hoocons_android.CustomUI.CustomFlowLayout;
import com.hoocons.hoocons_android.EventBus.TagsCollected;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetChannelTagsFragment extends Fragment {
    @BindView(R.id.topic_flow_layout)
    CustomFlowLayout mFlowLayout;
    @BindView(R.id.add_topic_btn)
    BootstrapButton mAddTopic;
    @BindView(R.id.meeting_tags_ed)
    EditText mInput;
    @BindView(R.id.gcn_next)
    Button mNext;

    private static final String CHANNEL_CATEGORY = "category";
    private List<String> topics;

    private String mCategory;


    public GetChannelTagsFragment() {
        // Required empty public constructor
    }

    public static GetChannelTagsFragment newInstance(String mCategory) {
        GetChannelTagsFragment fragment = new GetChannelTagsFragment();
        Bundle args = new Bundle();
        args.putString(CHANNEL_CATEGORY, mCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getString(CHANNEL_CATEGORY);
        }
        topics = new ArrayList<>();

        topics.add(mCategory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_get_channel_tags, container, false);
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
                EventBus.getDefault().post(new TagsCollected(topics));
            }
        });
    }

    private void addTopicView() {
        if (mInput.getText().length() > 0) {
            String topic = mInput.getText().toString();

            if (topics.contains(topic)) {
                Toast.makeText(getContext(), getResources().getText(R.string.already_created),
                        Toast.LENGTH_SHORT).show();
            } else {
                topics.add(topic);
                initFlowLayoutView();
            }

            mInput.setText("");
        }
    }

    private void initFlowLayoutView() {
        mFlowLayout.removeAllViews();

        for (int i = 0; i < topics.size(); i++) {
            final RelativeLayout item = (RelativeLayout) getLayoutInflater().inflate(R.layout.category_flow_item_layout,
                    mFlowLayout, false);
            TextView topic = (TextView) item.findViewById(R.id.topic_flow_text);

            topic.setText(topics.get(i));
            item.setTag(i);

            mFlowLayout.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag();
                    item.setVisibility(View.GONE);
                    updateTags(i);
                    topics.remove(i);

                    if (topics.size() == 0) {
                        mFlowLayout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void updateTags(int i) {
        for (i = i + 1; i < topics.size(); i++) {
            RelativeLayout flowChild = (RelativeLayout) mFlowLayout.getChildAt(i);
            int temp = (int) flowChild.getTag();
            flowChild.setTag(temp - 1);
        }
    }

}
