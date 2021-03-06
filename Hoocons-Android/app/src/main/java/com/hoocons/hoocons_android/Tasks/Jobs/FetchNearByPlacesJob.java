package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.FetchLocalPlacesComplete;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.GooglePlaceNetContext;
import com.hoocons.hoocons_android.Networking.Responses.GooglePlaceResponse;
import com.hoocons.hoocons_android.Networking.Services.GooglePlaceServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/26/17.
 */

public class FetchNearByPlacesJob extends Job implements Serializable {
    private String latitude;
    private String longitude;

    public FetchNearByPlacesJob(String latitude, String longitude) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.publicApi));
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        GooglePlaceServices services = GooglePlaceNetContext.instance.create(GooglePlaceServices.class);
        String location = latitude + "," + longitude;
        services.getAroundPlaces(location, "5000", MapUtils.getPlaceTypeQuery(),
                BaseApplication.getInstance().getGoogleServiceKey())
                .enqueue(new Callback<GooglePlaceResponse>() {
            @Override
            public void onResponse(Call<GooglePlaceResponse> call,
                                   Response<GooglePlaceResponse> response) {
                if (response.code() == 200) {
                    // 200 is complete
                    EventBus.getDefault().post(new FetchLocalPlacesComplete(response.body()));
                }
            }

            @Override
            public void onFailure(Call<GooglePlaceResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
