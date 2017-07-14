package com.hoocons.hoocons_android.Tasks;

import android.app.usage.UsageEvents;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

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
import com.hoocons.hoocons_android.Helpers.FirebaseConstant;
import com.hoocons.hoocons_android.Helpers.ImageEncoder;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.Networking.Services.UserServices;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class UpdateUserInfoTask extends AsyncTask<String, String, String> {
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
    protected void onPostExecute(String s) {
        uploadDataToServer(s);
    }

    @Override
    protected String doInBackground(String... params) {
        if (mImagePath != null) {
            try {
                return uploadUserProfile(mImagePath);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void uploadDataToServer(String profileUrl) {
        // Todo: Check location information here and push with it
        final String url = profileUrl != null? profileUrl : "";

        UserServices services = NetContext.instance.create(UserServices.class);
        services.updateUserInfo(new UserInformationRequest(displayName,
                nickname, gender, birthday, url, 0, 0))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            SharedPreferencesManager.getDefault()
                                    .setUserKeyInfo(new UserInfoResponse(displayName,
                                            nickname, url));
                            EventBus.getDefault().post(new CompleteLoginRequest());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        EventBus.getDefault().post(new BadRequest());
                    }
                });
    }

    private String uploadUserProfile(String mImagePath) throws InterruptedException {
        String userName = SharedPreferencesManager.getDefault().getUserName();
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        mStorage = mStorage.child(FirebaseConstant.STORAGE_PROFILE).child(userName);

        final ArrayList<String> _uploadedImages = new ArrayList<>();
        final CountDownLatch uploadDone = new CountDownLatch(1);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        String timeStamp = String.valueOf(new Date().getTime());
        String fileName = userName + "_" + timeStamp + ".jpg";

        byte[] arrBytes = ImageEncoder.encodeImage(mImagePath);

        UploadTask uploadTask = mStorage.child(fileName).putBytes(arrBytes, metadata);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uploadDone.countDown();
                EventBus.getDefault().post(new UploadImageFailed());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadDone.countDown();
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                _uploadedImages.add(downloadUrl.toString());
            }
        });

        uploadDone.await();

        if (_uploadedImages.size() > 0) {
            return _uploadedImages.get(0);
        }

        return null;
    }
}
