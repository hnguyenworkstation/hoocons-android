package com.hoocons.hoocons_android.Parcel;

import android.os.Parcel;

import com.hoocons.hoocons_android.Models.Topic;
import com.hoocons.hoocons_android.Networking.Responses.TagResponse;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 8/18/17.
 */

public class MultiTopicsParcel implements ParcelConverter<List<Topic>> {
    @Override
    public void toParcel(List<Topic> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (Topic item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public List<Topic> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        List<Topic> items = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            items.add((Topic) Parcels.unwrap(parcel.readParcelable(Topic.class.getClassLoader())));
        }

        return items;
    }
}
