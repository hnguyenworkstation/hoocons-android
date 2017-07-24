package com.hoocons.hoocons_android.Parcel;

import android.os.Parcel;

import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;
import com.hoocons.hoocons_android.Networking.Responses.TagResponse;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 7/24/17.
 */
public class MultiTagsParcel implements ParcelConverter<List<TagResponse>> {
    @Override
    public void toParcel(List<TagResponse> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (TagResponse item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public List<TagResponse> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        List<TagResponse> items = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            items.add((TagResponse) Parcels.unwrap(parcel.readParcelable(TagResponse.class.getClassLoader())));
        }

        return items;
    }
}
