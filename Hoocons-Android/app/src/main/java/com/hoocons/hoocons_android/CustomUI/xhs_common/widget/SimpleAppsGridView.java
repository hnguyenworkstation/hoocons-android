package com.hoocons.hoocons_android.CustomUI.xhs_common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.hoocons.hoocons_android.CustomUI.xhs_common.adapter.AppsAdapter;
import com.hoocons.hoocons_android.CustomUI.xhs_common.data.AppBean;
import com.hoocons.hoocons_android.R;

import java.util.ArrayList;

public class SimpleAppsGridView extends RelativeLayout {

    protected View view;

    public SimpleAppsGridView(Context context) {
        this(context, null);
    }

    public SimpleAppsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_apps, this);
        init();
    }

    protected void init(){
        GridView gv_apps = (GridView) view.findViewById(R.id.gv_apps);
        ArrayList<AppBean> mAppBeanList = new ArrayList<>();
        mAppBeanList.add(new AppBean(R.mipmap.icon_login_phone_blue, "test"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_login_phone_blue, "test"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_login_phone_blue, "test"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_login_phone_blue, "test"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_login_phone_blue, "test"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_login_phone_blue, "test"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_login_phone_blue, "test"));
        AppsAdapter adapter = new AppsAdapter(getContext(), mAppBeanList);
        gv_apps.setAdapter(adapter);
    }
}
