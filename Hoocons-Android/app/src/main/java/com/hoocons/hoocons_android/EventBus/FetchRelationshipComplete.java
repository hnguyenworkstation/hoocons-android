package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.Responses.RelationshipResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 8/26/17.
 */

public class FetchRelationshipComplete {
    private List<RelationshipResponse> relationshipResponseList;

    public FetchRelationshipComplete(List<RelationshipResponse> relationshipResponseList) {
        if (relationshipResponseList == null) {
            this.relationshipResponseList = new ArrayList<>();
        } else {
            this.relationshipResponseList = relationshipResponseList;
        }
    }

    public List<RelationshipResponse> getRelationshipResponseList() {
        return relationshipResponseList;
    }

    public void setRelationshipResponseList(List<RelationshipResponse> relationshipResponseList) {
        this.relationshipResponseList = relationshipResponseList;
    }
}
