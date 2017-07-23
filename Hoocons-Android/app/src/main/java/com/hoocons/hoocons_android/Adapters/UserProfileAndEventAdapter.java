package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Interface.EventAdapterListener;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.UserInfoAndEventViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class UserProfileAndEventAdapter extends RecyclerView.Adapter<UserInfoAndEventViewHolder> {
    private List<EventResponse> responseList;
    private Context context;

    private final int EVENT_LOADING_END = 100;

    private final int TYPE_EVENT_TEXT = 0;
    private final int TYPE_EVENT_SINGLE_IMAGE = 1;
    private final int TYPE_EVENT_MUL_IMAGES = 2;
    private final int TYPE_EVENT_VIDEO= 3;
    private final int TYPE_EVENT_RELOAD = 4;
    private final int TYPE_EVENT_WEB = 5;
    private final int TYPE_EVENT_CHECKIN = 6;

    private final int EVENT_DUMMIES_CARD = 10;

    private final int USER_INFO_TAG_CARD = -1;
    private boolean isMyself;
    private EventAdapterListener listener;

    private final int EXTRA_ITEMS = 2;
    private final Handler handler = new Handler();

    public UserProfileAndEventAdapter(Context context, List<EventResponse> responsesList,
                                      final EventAdapterListener listener, boolean isMyself) {
        this.context = context;
        this.responseList = responsesList;
        this.isMyself = isMyself;
        this.listener = listener;
    }

    @Override
    public UserInfoAndEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case EVENT_DUMMIES_CARD:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_profile_dummy_viewholder, parent, false);
                break;
            case USER_INFO_TAG_CARD:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_info_profile_viewholder, parent, false);
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
            default:
                break;
        }

        return new UserInfoAndEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserInfoAndEventViewHolder holder, int position) {
        if (position == 0) {
            holder.initUserInfo(context, isMyself);
        } else if (position == 1) {
            if (responseList.size() > 0) {
                holder.initDummyCardForEvent(context, isMyself,
                        responseList.get(0).getUserInfo().getDisplayName());
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
            return USER_INFO_TAG_CARD;
        } else if (position == 1) {
            return EVENT_DUMMIES_CARD;
        }

        if (responseList.get(position - EXTRA_ITEMS) == null) {
            return EVENT_LOADING_END;
        }

        EventResponse response = responseList.get(position - EXTRA_ITEMS);
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
