package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Topic;

import java.util.List;

/**
 * Created by hungnguyen on 9/3/17.
 */

public class HobbiesRequest {
    @SerializedName("hobbies")
    private List<Topic> hobbies;

    public HobbiesRequest(List<Topic> hobbies) {
        this.hobbies = hobbies;
    }

    public List<Topic> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Topic> hobbies) {
        this.hobbies = hobbies;
    }
}
