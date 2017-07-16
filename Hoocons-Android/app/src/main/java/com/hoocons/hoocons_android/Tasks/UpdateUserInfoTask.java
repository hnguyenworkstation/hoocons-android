package com.hoocons.hoocons_android.Tasks;

import android.app.usage.UsageEvents;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.CompleteLoginRequest;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.EventBus.UploadImageFailed;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.FirebaseConstant;
import com.hoocons.hoocons_android.Helpers.ImageEncoder;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.Networking.Services.UserServices;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class UpdateUserInfoTask extends AsyncTask<String, String, Media> {
    private final String TAG = UpdateUserInfoTask.class.getSimpleName();
    private String displayName;
    private String nickname;
    private String gender;
    private String birthday;
    private String mImagePath;

    public UpdateUserInfoTask(String displayName, String nickname, String gender,
                              String birthday, String mImagePath) {
        this.displayName = displayName;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.mImagePath = mImagePath;
    }

    @Override
    protected void onPostExecute(Media s) {
        uploadDataToServer(s);
    }

    @Override
    protected Media doInBackground(String... params) {
        if (mImagePath != null) {
            return updateImageToS3(mImagePath);
        }

        return null;
    }

    private void uploadDataToServer(final Media profileMedia) {
        UserServices services = NetContext.instance.create(UserServices.class);
        services.updateUserInfo(new UserInformationRequest(displayName,
                nickname, gender, birthday, profileMedia, 0, 0))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            if (profileMedia == null) {
                                SharedPreferencesManager.getDefault()
                                        .setUserKeyInfo(new UserInfoResponse(displayName,
                                                nickname, AppUtils.getDefaultProfileUrl()));
                            } else {
                                SharedPreferencesManager.getDefault()
                                        .setUserKeyInfo(new UserInfoResponse(displayName,
                                                nickname, profileMedia.getUrl()));
                            }

                            EventBus.getDefault().post(new CompleteLoginRequest());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        EventBus.getDefault().post(new BadRequest());
                    }
                });
    }

    @Nullable
    private Media updateImageToS3(String imagePath) {
        try {
            String s3 = BaseApplication.getInstance().getS3AWS();
            Log.e(TAG, "updateImageToS3: " +  s3);
            String timeStamp = String.valueOf(new Date().getTime());

            AmazonS3Client s3Client = BaseApplication.getInstance().getAwsS3Client();

            String fileName = AppUtils.encryptMsg(timeStamp) + ".png";

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
