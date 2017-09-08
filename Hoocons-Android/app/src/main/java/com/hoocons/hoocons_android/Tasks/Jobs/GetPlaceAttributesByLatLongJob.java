package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.InvalidLocationRequest;
import com.hoocons.hoocons_android.EventBus.LocationRequestCollected;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.GooglePlaceNetContext;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;
import com.hoocons.hoocons_android.Networking.Responses.CoordinateResponse;
import com.hoocons.hoocons_android.Networking.Responses.GooglePlaceAttributes;
import com.hoocons.hoocons_android.Networking.Responses.GooglePlaceInformation;
import com.hoocons.hoocons_android.Networking.Services.GooglePlaceServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 9/8/17.
 */

public class GetPlaceAttributesByLatLongJob extends Job {
    private double latitude;
    private double longitude;
    private String tag;

    public GetPlaceAttributesByLatLongJob(double latitude, double longitude, String tag) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.publicApi));
        this.latitude = latitude;
        this.longitude = longitude;
        this.tag = tag;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        GooglePlaceServices placeNetContext = GooglePlaceNetContext.instance.create(GooglePlaceServices.class);
        String latlng = String.format("%s, %s", String.valueOf(latitude), String.valueOf(longitude));
        placeNetContext.getLocationAttributeByLatLng(latlng,
                BaseApplication.getInstance().getGoogleServiceKey())
                .enqueue(new Callback<GooglePlaceInformation>() {
                    @Override
                    public void onResponse(Call<GooglePlaceInformation> call, Response<GooglePlaceInformation> response) {
                        if (response.code() == 200) {
                            if (response.body().getResults() != null && response.body().getResults().size() > 0) {
                                postLocationRequestData(response.body().getResults().get(0));
                            } else {
                                EventBus.getDefault().post(new InvalidLocationRequest());
                            }
                        } else {
                            EventBus.getDefault().post(new InvalidLocationRequest());
                        }
                    }

                    @Override
                    public void onFailure(Call<GooglePlaceInformation> call, Throwable t) {
                        EventBus.getDefault().post(new InvalidLocationRequest());
                    }
                });
    }

    private void postLocationRequestData(GooglePlaceAttributes attributes) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setResponse(new CoordinateResponse(latitude, longitude));
        for (GooglePlaceAttributes.AddressComponents component:attributes.getAddressComponents()) {
            if (component.getTypes().contains("administrative_area_level_2")) {
                locationRequest.setCity(component.getLongName());
            } else if (component.getTypes().contains("administrative_area_level_1")) {
                locationRequest.setState(component.getLongName());
            } else if (component.getTypes().contains("country")) {
                locationRequest.setCountry(component.getLongName());
            } else if (component.getTypes().contains("postal_code")) {
                locationRequest.setZipcode(component.getLongName());
            }
        }

        EventBus.getDefault().post(new LocationRequestCollected(locationRequest, tag));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
