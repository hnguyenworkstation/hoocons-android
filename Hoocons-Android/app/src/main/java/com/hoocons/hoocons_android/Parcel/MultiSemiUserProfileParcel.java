package com.hoocons.hoocons_android.Parcel;

import android.os.Parcel;

import com.hoocons.hoocons_android.Networking.Responses.SemiUserInfoResponse;
import com.hoocons.hoocons_android.Networking.Responses.TagResponse;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 9/8/17.
 */

public class MultiSemiUserProfileParcel implements ParcelConverter<List<SemiUserInfoResponse>> {
    @Override
    public void toParcel(List<SemiUserInfoResponse> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (SemiUserInfoResponse item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public List<SemiUserInfoResponse> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        List<SemiUserInfoResponse> items = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            items.add((SemiUserInfoResponse) Parcels.unwrap(parcel.readParcelable(SemiUserInfoResponse.class.getClassLoader())));
        }

        return items;
    }
}
