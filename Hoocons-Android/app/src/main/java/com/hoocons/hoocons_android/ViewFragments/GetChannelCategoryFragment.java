package com.hoocons.hoocons_android.ViewFragments;

import android.content.Context;
import android.net.Uri;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.hoocons.hoocons_android.CustomUI.CustomFlowLayout;
import com.hoocons.hoocons_android.EventBus.ChannelCategoryCollected;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GetChannelCategoryFragment extends Fragment {
    @BindView(R.id.suggested_cat)
    CustomFlowLayout mFlowLayout;
    @BindView(R.id.input)
    TextView mInput;
    @BindView(R.id.next)
    Button mNextButton;

    private String category;

    private final String UNABLE_FLOW = "unable_flow";
    private final String AVAILABLE_FLOW = "available_flow";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String[] mVals = new String[]
            {"Traveling", "Services", "Beauty", "Websites & Blogs"};

    private LayoutInflater mInflater;

    public GetChannelCategoryFragment() {
        // Required empty public constructor
    }

    public static GetChannelCategoryFragment newInstance(String param1, String param2) {
        GetChannelCategoryFragment fragment = new GetChannelCategoryFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_channel_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mInflater = LayoutInflater.from(getContext());

        initView();
    }

    private void initView() {
        initFlowLayout();

        mInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLongList();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category == null) {
                    Toast.makeText(getContext(), getContext().getResources()
                                    .getString(R.string.please_choose_a_category)
                            , Toast.LENGTH_SHORT).show();
                } else {
                    EventBus.getDefault().post(new ChannelCategoryCollected(category));
                }
            }
        });
    }

    private void showLongList() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.category)
                .items(R.array.channel_category)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView,
                                            int position, CharSequence text) {
                        setCategoryText(text.toString());
                    }
                })
                .positiveText(android.R.string.cancel)
                .show();
    }

    private void setCategoryText(String text) {
        mInput.setText(text);
        mInput.setTextColor(getContext().getResources().getColor(R.color.black));
        category = text;
    }

    private void initFlowLayout() {
        for (int i = 0; i < mVals.length; i++) {
            final RelativeLayout item = (RelativeLayout) mInflater.inflate(R.layout.category_flow_item_layout,
                    mFlowLayout, false);
            TextView topic = (TextView) item.findViewById(R.id.topic_flow_text);
            topic.setText(mVals[i]);
            item.setTag(i);
            mFlowLayout.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag();
                    setCategoryText(mVals[i]);
                }
            });

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
