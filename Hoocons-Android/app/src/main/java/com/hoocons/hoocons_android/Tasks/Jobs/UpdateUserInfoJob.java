package com.hoocons.hoocons_android.Tasks.Jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.CompleteLoginRequest;
import com.hoocons.hoocons_android.EventBus.JobFailureEvBusRequest;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Models.Topic;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.Networking.Services.UserServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/16/17.
 */

public class UpdateUserInfoJob extends Job implements Serializable {
    private final String TAG = UpdateUserInfoJob.class.getSimpleName();

    private String gender;
    private String displayName;
    private String nickname;
    private String birthday;
    private String work;
    private String profileUrl;
    private String wallpaperUrl;
    private List<String> hobbies;
    private double latitude;
    private double longitude;
    private LocationRequest locationRequest;

    public UpdateUserInfoJob(LocationRequest locationRequest, String gender, String displayName, String nickname,
                             String birthday, String work, String profileUrl, String wallpaperUrl, List<String> hobbies,
                             double latitude, double longitude) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.userInfo));
        this.locationRequest = locationRequest;
        this.gender = gender;
        this.displayName = displayName;
        this.nickname = nickname;
        this.birthday = birthday;
        this.work = work;
        this.profileUrl = profileUrl;
        this.wallpaperUrl = wallpaperUrl;
        this.hobbies = hobbies;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        uploadDataToServer();
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }


    private void uploadDataToServer() {
        if (locationRequest != null) {
            List<Topic> hobbyTopics = new ArrayList<>();
            for (String hobby: hobbies) {
                hobbyTopics.add(new Topic(hobby));
            }

            UserServices services = NetContext.instance.create(UserServices.class);
            services.updateUserInfo(SharedPreferencesManager.getDefault().getUserId(),
                    new UserInformationRequest(gender, displayName,
                            nickname, birthday, work, new Media(profileUrl, AppConstant.MEDIA_TYPE_IMAGE),
                            wallpaperUrl == null? null: new Media(wallpaperUrl, AppConstant.MEDIA_TYPE_IMAGE),
                            locationRequest, hobbyTopics))
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                SharedPreferencesManager.getDefault()
                                        .setUserKeyInfo(new UserInfoResponse(displayName,
                                                nickname, profileUrl));
                                EventBus.getDefault().post(new CompleteLoginRequest());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            EventBus.getDefault().post(new ServerErrorRequest());
                        }
                    });
        } else {
            EventBus.getDefault().post(new JobFailureEvBusRequest());
        }
    }


    @Nullable
    private Media updateImageToS3(String imagePath) {
        try {
            String s3 = BaseApplication.getInstance().getS3AWS();
            Log.e(TAG, "updateImageToS3: " +  s3);
            String timeStamp = String.valueOf(new Date().getTime());

            AmazonS3Client s3Client = BaseApplication.getInstance().getAwsS3Client();

            String fileName = AppUtils.getRandomSaltString() + "_" + timeStamp + ".png";

            PutObjectRequest por = new PutObjectRequest(s3 + "/profiles",
                    fileName, new File(imagePath));
            por.setCannedAcl(CannedAccessControlList.PublicRead);

            s3Client.putObject(por);
            String _finalUrl = "https://s3-ap-southeast-1.amazonaws.com/"
                    + s3 + "/profiles/" + fileName;

            return new Media(_finalUrl, AppConstant.MEDIA_TYPE_IMAGE);
        } catch (Exception e) {
            Log.e(TAG, "updateImageToS3: " +  e.toString());
        }

        return null;
    }
}
