package com.hoocons.hoocons_android.Networking.ApiViewSets;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Responses.FriendshipRequestResponse;

import java.util.List;

/**
 * Created by hungnguyen on 8/24/17.
 */

public class FriendshipRequestApiViewSet {
    @SerializedName("count")
    private int count;
    @SerializedName("next")
    private String next;
    @SerializedName("previous")
    private String previous;
    @SerializedName("results")
    private List<FriendshipRequestResponse> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<FriendshipRequestResponse> getResults() {
        return results;
    }

    public void setResults(List<FriendshipRequestResponse> results) {
        this.results = results;
    }
}
