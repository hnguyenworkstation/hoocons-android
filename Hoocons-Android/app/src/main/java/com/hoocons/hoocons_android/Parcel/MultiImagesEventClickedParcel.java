package com.hoocons.hoocons_android.Parcel;

import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.List;

/**
 * Created by hungnguyen on 7/23/17.
 */
@Parcel
public class MultiImagesEventClickedParcel {
    String textContent;
    String userDisplayName;

    @ParcelPropertyConverter(MediaListParcel.class)
    List<MediaResponse> responseList;

    int clickedPosition;

    public MultiImagesEventClickedParcel() {

    }

    public MultiImagesEventClickedParcel(List<MediaResponse> responseList,
                                         int clickedPosition, String textContent, String userDisplayName) {
        this.responseList = responseList;
        this.clickedPosition = clickedPosition;
        this.textContent = textContent;
        this.userDisplayName = userDisplayName;
    }

    public List<MediaResponse> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<MediaResponse> responseList) {
        this.responseList = responseList;
    }

    public int getClickedPosition() {
        return clickedPosition;
    }

    public void setClickedPosition(int clickedPosition) {
        this.clickedPosition = clickedPosition;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }
}
