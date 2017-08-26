package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 8/26/17.
 */

public class RelationshipResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("with_user")
    private SemiUserInfoResponse withUser;
    @SerializedName("rel_made_at")
    private String relMadeAt;
    @SerializedName("status")
    private String status;

    public RelationshipResponse(int id, SemiUserInfoResponse withUser, String relMadeAt, String status) {
        this.id = id;
        this.withUser = withUser;
        this.relMadeAt = relMadeAt;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SemiUserInfoResponse getWithUser() {
        return withUser;
    }

    public void setWithUser(SemiUserInfoResponse withUser) {
        this.withUser = withUser;
    }

    public String getRelMadeAt() {
        return relMadeAt;
    }

    public void setRelMadeAt(String relMadeAt) {
        this.relMadeAt = relMadeAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
