package com.hoocons.hoocons_android.Parcel;

import android.os.Parcel;

import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 7/23/17.
 */
public class MediaListParcel implements ParcelConverter<List<MediaResponse>> {
    @Override
    public void toParcel(List<MediaResponse> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (MediaResponse item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public List<MediaResponse> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        List<MediaResponse> items = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            items.add((MediaResponse) Parcels.unwrap(parcel.readParcelable(MediaResponse.class.getClassLoader())));
        }
        return items;
    }
}
