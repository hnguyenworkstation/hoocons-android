package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.PostingJobAddedToDisk;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Models.Topic;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 7/29/17.
 */

public class NewMeetoutJob extends Job implements Serializable {
    private String meetoutName;
    private String meetoutDesc;

    private double meetingLong;
    private double meetingLat;
    private double currentLong;
    private double currentLat;

    private String locationName;
    private String locationAddress;
    private String fromDateTime;
    private String toDateTime;

    private List<String> imagePaths;
    private List<String> topics;

    public NewMeetoutJob(String meetoutName, String meetoutDesc,
                         double meetingLong, double meetingLat, double currentLong,
                         double currentLat, String locationName, String locationAddress,
                         String fromDateTime, String toDateTime,
                         List<String> imagePaths, List<String> topics) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.meetout));
        this.meetoutName = meetoutName;
        this.meetoutDesc = meetoutDesc;
        this.meetingLong = meetingLong;
        this.meetingLat = meetingLat;
        this.currentLong = currentLong;
        this.currentLat = currentLat;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.imagePaths = imagePaths;
        this.topics = topics;
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new PostingJobAddedToDisk());
    }

    @Override
    public void onRun() throws Throwable {
        ArrayList<Media> medias = new ArrayList<>();
        if (imagePaths != null && imagePaths.size() > 0) {
            medias = AppUtils.uploadAllMeetOutImages(imagePaths);
        }

        List<Topic> meetoutTopics = new ArrayList<>();
        if (topics != null && topics.size() > 0) {
            for (String topic : topics) {
                meetoutTopics.add(new Topic(topic));
            }
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
