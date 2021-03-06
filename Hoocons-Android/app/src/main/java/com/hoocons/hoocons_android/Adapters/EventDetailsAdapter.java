package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Interface.EventAdapterListener;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.EventDetailsViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */
public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsViewHolder> {
    private List<EventResponse> responseList;
    private Context context;

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

    private final int EVENT_DUMMIES_CARD = 99;
    private final int USER_OTHER_INFO = 98;

    private final int USER_INFO_TAG_CARD = -1;
    private EventAdapterListener listener;

    private final int EXTRA_ITEMS = 1;
    private final Handler handler = new Handler();

    public EventDetailsAdapter(Context context, List<EventResponse> responsesList,
                               final EventAdapterListener listener) {
        this.context = context;
        this.responseList = responsesList;
        this.listener = listener;
    }

    @Override
    public void onViewRecycled(EventDetailsViewHolder holder) {
        holder.onViewRecycled();
        super.onViewRecycled(holder);
    }

    @Override
    public EventDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case EVENT_DUMMIES_CARD:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_profile_dummy_viewholder, parent, false);
                break;
            case USER_OTHER_INFO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.empty_recycler_viewholder, parent, false);
                break;
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
            case EVENT_LOADING_END:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_loading_image, parent, false);
                break;
            case TYPE_SHARED_EVENT_TEXT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shared_event_text_viewholder, parent, false);
                break;
            case TYPE_SHARED_EVENT_SINGLE_IMAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shared_event_single_media_viewholder, parent, false);
                break;
            case TYPE_SHARED_EVENT_MUL_IMAGES:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shared_event_multi_medias_viewholder, parent, false);
                break;
            case TYPE_SHARED_EVENT_VIDEO:
                break;
            case TYPE_SHARED_EVENT_WEB:
                break;
            case TYPE_SHARED_EVENT_CHECKIN:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shared_event_checkin_viewholder, parent, false);
                break;
            default:
                break;
        }

        return new EventDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventDetailsViewHolder holder, int position) {
        if (position == 0) {
            if (responseList != null && responseList.size() > 0) {
                holder.initDummyCardForEvent(context,
                        SharedPreferencesManager.getDefault().getUserId() == responseList.get(position)
                                .getAuthor().getUser(),
                        responseList.get(0).getAuthor().getDisplayName());
            }
        } else {
            if (responseList.get(position - EXTRA_ITEMS) != null) {
                holder.initViewHolder(context, responseList.get(position - EXTRA_ITEMS),
                        listener, position - EXTRA_ITEMS);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return EVENT_DUMMIES_CARD;
        }

        EventResponse response = responseList.get(position - EXTRA_ITEMS);
        if (response == null && responseList.size() > 0) {
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

    public int getEXTRA_ITEMS() {
        return EXTRA_ITEMS;
    }

    @Override
    public int getItemCount() {
        return responseList.size() + EXTRA_ITEMS;
    }

    public void addLoadingFooter() {
        final Runnable r = new Runnable() {
            public void run() {
                responseList.add(null);
                notifyItemInserted(responseList.size() + EXTRA_ITEMS - 1);
            }
        };

        handler.post(r);
    }

    public void removeLoadingFooter() {
        final Runnable r = new Runnable() {
            public void run() {
                responseList.remove(responseList.size() - 1);
                notifyItemRemoved(responseList.size() + EXTRA_ITEMS);
            }
        };

        handler.post(r);
    }
}
