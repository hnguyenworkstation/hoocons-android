package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.EventViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private List<EventResponse> responseList;
    private Context context;

    private final int TYPE_EVENT_TEXT = 0;
    private final int TYPE_EVENT_SINGLE_IMAGE = 1;
    private final int TYPE_EVENT_MUL_IMAGES = 2;
    private final int TYPE_EVENT_VIDEO= 3;
    private final int TYPE_EVENT_RELOAD = 4;
    private final int TYPE_EVENT_WEB = 5;
    private final int TYPE_EVENT_CHECKIN = 6;

    public EventAdapter(Context context, List<EventResponse> responsesList) {
        this.context = context;
        this.responseList = responsesList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_EVENT_TEXT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_text_only_viewholder, parent, false);
                break;
            case TYPE_EVENT_SINGLE_IMAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_single_media_viewholder, parent, false);
                break;
            case TYPE_EVENT_MUL_IMAGES:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_multi_media_viewholder, parent, false);
                break;
            case TYPE_EVENT_CHECKIN:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_check_in_viewholder, parent, false);
                break;
            default:
                break;
        }

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.initViewHolder(responseList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        EventResponse response = responseList.get(position);
        switch (response.getEventType()) {
            case AppConstant.EVENT_TYPE_TEXT:
                return TYPE_EVENT_TEXT;
            case AppConstant.EVENT_TYPE_SINGLE_IMAGE:
                return TYPE_EVENT_SINGLE_IMAGE;
            case AppConstant.EVENT_TYPE_MULT_IMAGE:
                return TYPE_EVENT_MUL_IMAGES;
            case AppConstant.EVENT_TYPE_CHECK_IN:
                return TYPE_EVENT_CHECKIN;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public void addLoadingFooter() {
        responseList.add(null);
        notifyItemInserted(responseList.size() - 1);
    }

    public void removeLoadingFooter() {
        responseList.remove(responseList.size() - 1);
        notifyItemRemoved(responseList.size());
    }
}
