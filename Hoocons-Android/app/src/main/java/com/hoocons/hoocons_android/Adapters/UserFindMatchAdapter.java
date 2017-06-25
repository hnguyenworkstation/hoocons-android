package com.hoocons.hoocons_android.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoocons.hoocons_android.CustomUI.swipe_cards.CardEntity;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.CardImageView;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.CustomUserCardView;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.ImageLoaderHandler;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.SwipeIndicatorView;
import com.hoocons.hoocons_android.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 6/25/17.
 */

public class UserFindMatchAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<CardEntity> mList;

    public UserFindMatchAdapter(Context context, ArrayList<CardEntity> list) {
        mInflater = LayoutInflater.from(context);
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.swipe_fling_item, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        CardEntity cardEntity = ((CardEntity) getItem(position));
        holder.likeIndicator.reset();
        holder.unLikeIndicator.reset();
        holder.nameView.setText(cardEntity.who);
        holder.addressView.setText(cardEntity.desc);
        holder.img.reset();
        holder.img.setUser(cardEntity);
        ImageLoaderHandler.get().loadCardImage((Activity) mContext, holder.img, null, cardEntity.url, false);
        return convertView;
    }

    static class ViewHolder {
        CustomUserCardView cardLayout;
        CardImageView img;
        TextView nameView;
        TextView addressView;
        SwipeIndicatorView likeIndicator;
        SwipeIndicatorView unLikeIndicator;
        TextView mFriendCountTv;
        TextView mInterestCountTv;
        ViewGroup mBottomLayout;

        ViewHolder(View rootView) {
            cardLayout = (CustomUserCardView) rootView;
            img = ButterKnife.findById(rootView, R.id.item_img);
            nameView = ButterKnife.findById(rootView, R.id.item_name);
            addressView = ButterKnife.findById(rootView, R.id.item_address);
            likeIndicator = ButterKnife.findById(rootView, R.id.item_swipe_like_indicator);
            unLikeIndicator = ButterKnife.findById(rootView, R.id.item_swipe_unlike_indicator);
            mFriendCountTv = ButterKnife.findById(rootView, R.id.item_friend_count);
            mInterestCountTv = ButterKnife.findById(rootView, R.id.item_interest_count);
            mBottomLayout = ButterKnife.findById(rootView, R.id.item_bottom_layout);
        }

        @Override
        public String toString() {
            return "[Card:" + nameView.getText() + "]";
        }
    }

}
