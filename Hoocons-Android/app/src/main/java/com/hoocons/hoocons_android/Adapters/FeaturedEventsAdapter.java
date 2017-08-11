package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Interface.EventAdapterListener;
import com.hoocons.hoocons_android.Networking.Responses.ActivityResponse;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.FeaturedEventViewHolder;
import com.hoocons.hoocons_android.ViewHolders.UserRelatedDetailsViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 8/11/17.
 */

public class FeaturedEventsAdapter extends RecyclerView.Adapter<FeaturedEventViewHolder> {
    private Context context;
    private List<ActivityResponse> activityResponseList;
    private EventAdapterListener listener;

    private final int EVENT_LOADING_END = 100;

    private final int TYPE_EVENT_TEXT = 0;
    private final int TYPE_EVENT_SINGLE_IMAGE = 1;
    private final int TYPE_EVENT_MUL_IMAGES = 2;
    private final int TYPE_EVENT_VIDEO= 3;
    private final int TYPE_EVENT_WEB = 4;
    private final int TYPE_EVENT_CHECKIN = 5;

    private final int TYPE_SHARED_EVENT_TEXT = 6;
    private final int TYPE_SHARED_EVENT_SINGLE_IMAGE = 7;
    private final int TYPE_SHARED_EVENT_MUL_IMAGES = 8;
    private final int TYPE_SHARED_EVENT_VIDEO= 9;
    private final int TYPE_SHARED_EVENT_WEB = 10;
    private final int TYPE_SHARED_EVENT_CHECKIN = 11;

    private final int OTHER_INFO_LIST = 20;

    private final Handler handler = new Handler();

    @Override
    public FeaturedEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case OTHER_INFO_LIST:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.empty_recycler_viewholder, parent, false);
                break;
            case TYPE_EVENT_TEXT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.featured_event_text_only_viewholder, parent, false);
                break;
            case TYPE_EVENT_SINGLE_IMAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.featured_event_single_media_viewholder, parent, false);
                break;
            case TYPE_EVENT_MUL_IMAGES:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.featured_event_multi_media_viewholder, parent, false);
                break;
            case TYPE_EVENT_CHECKIN:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.featured_event_checkin_viewholder, parent, false);
                break;
            case EVENT_LOADING_END:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_loading_image, parent, false);
                break;
            case TYPE_SHARED_EVENT_TEXT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.featured_shared_event_text_viewholder, parent, false);
                break;
            case TYPE_SHARED_EVENT_SINGLE_IMAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.featured_shared_event_single_media_viewholder, parent, false);
                break;
            case TYPE_SHARED_EVENT_MUL_IMAGES:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.featured_shared_event_multi_medias_viewholder, parent, false);
                break;
            case TYPE_SHARED_EVENT_VIDEO:
                break;
            case TYPE_SHARED_EVENT_WEB:
                break;
            case TYPE_SHARED_EVENT_CHECKIN:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.featured_shared_event_checkin_viewholder, parent, false);
                break;
            default:
                break;
        }

        return new FeaturedEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeaturedEventViewHolder holder, int position) {
        holder.initViewHolder(context, activityResponseList.get(position).getTarget(),
                listener, position);
    }

    @Override
    public int getItemCount() {
        return activityResponseList.size();
    }

    public int getItemViewType(int position) {
        EventResponse response = activityResponseList.get(position).getTarget();
        if (response == null && activityResponseList.size() > 0) {
            return EVENT_LOADING_END;
        }

        assert response != null;
        if (response.getContainEvent() == null) {
            switch (response.getEventType()) {
                case AppConstant.EVENT_TYPE_TEXT:
                    return TYPE_EVENT_TEXT;

                case AppConstant.EVENT_TYPE_SINGLE_IMAGE:
                    return TYPE_EVENT_SINGLE_IMAGE;

                case AppConstant.EVENT_TYPE_SINGLE_GIF:
                    return TYPE_EVENT_SINGLE_IMAGE;

                case AppConstant.EVENT_TYPE_MULT_IMAGE:
                    return TYPE_EVENT_MUL_IMAGES;

                case AppConstant.EVENT_TYPE_CHECK_IN:
                    return TYPE_EVENT_CHECKIN;

                default:
                    return -1;
            }
        } else {
            switch (response.getContainEvent().getEventType()) {
                case AppConstant.EVENT_TYPE_TEXT:
                    return TYPE_SHARED_EVENT_TEXT;

                case AppConstant.EVENT_TYPE_SINGLE_IMAGE:
                    return TYPE_SHARED_EVENT_SINGLE_IMAGE;

                case AppConstant.EVENT_TYPE_SINGLE_GIF:
                    return TYPE_SHARED_EVENT_SINGLE_IMAGE;

                case AppConstant.EVENT_TYPE_MULT_IMAGE:
                    return TYPE_SHARED_EVENT_MUL_IMAGES;

                case AppConstant.EVENT_TYPE_CHECK_IN:
                    return TYPE_SHARED_EVENT_CHECKIN;

                default:
                    return -1;
            }
        }
    }

    public void addLoadingFooter() {
        final Runnable r = new Runnable() {
            public void run() {
                activityResponseList.add(null);
                notifyItemInserted(activityResponseList.size() - 1);
            }
        };

        handler.post(r);
    }

    public void removeLoadingFooter() {
        final Runnable r = new Runnable() {
            public void run() {
                activityResponseList.remove(activityResponseList.size() - 1);
                notifyItemRemoved(activityResponseList.size());
            }
        };

        handler.post(r);
    }
}
