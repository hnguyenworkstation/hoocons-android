package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Networking.Responses.MeetOutResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.MeetOutCardViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 7/29/17.
 */

public class MeetOutsAdapter extends RecyclerView.Adapter<MeetOutCardViewHolder> {
    private List<MeetOutResponse> responseList;
    private Context context;

    public MeetOutsAdapter(List<MeetOutResponse> responseList, Context context) {
        this.responseList = responseList;
        this.context = context;
    }

    @Override
    public MeetOutCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meetout_card_viewholder, parent, false);
        return new MeetOutCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeetOutCardViewHolder holder, int position) {
        holder.initCardViewHolder(context, responseList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }
}
