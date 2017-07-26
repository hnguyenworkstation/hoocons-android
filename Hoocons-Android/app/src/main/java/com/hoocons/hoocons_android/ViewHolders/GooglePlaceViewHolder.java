package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Interface.OnGooglePlaceClickListener;
import com.hoocons.hoocons_android.Networking.Responses.GooglePlace;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 7/26/17.
 */

public class GooglePlaceViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.place_type_icon)
    ImageView mPlaceTypeIcon;
    @BindView(R.id.place_name)
    TextView mPlaceName;
    @BindView(R.id.place_address)
    TextView mPlaceAddress;

    public GooglePlaceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initView(final Context context, final GooglePlace place,
                         final OnGooglePlaceClickListener listener, final int position) {
        AppUtils.loadCircleImage(context, place.getIcon(), mPlaceTypeIcon);
        mPlaceName.setText(place.getName());
        mPlaceAddress.setText(place.getVicinity());
    }
}
