package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Interface.OnGooglePlaceClickListener;
import com.hoocons.hoocons_android.Networking.Responses.GooglePlace;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.GooglePlaceViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 7/26/17.
 */

public class GooglePlacesAdapter extends RecyclerView.Adapter<GooglePlaceViewHolder> {
    private List<GooglePlace> googlePlaces;
    private Context context;
    private OnGooglePlaceClickListener listener;

    public GooglePlacesAdapter(Context context, List<GooglePlace> places, OnGooglePlaceClickListener listener) {
        this.googlePlaces = places;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public GooglePlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.google_place_viewholder, parent, false);

        return new GooglePlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GooglePlaceViewHolder holder, int position) {
        holder.initView(context, googlePlaces.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return googlePlaces.size();
    }
}
