package com.hoocons.hoocons_android.DumbData;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.BaseModel;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.CardEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by hungnguyen on 6/25/17.
 */

public class TestUserData {
    public static ArrayList<CardEntity> getApiData(Context context) {
        BaseModel<ArrayList<CardEntity>> model = null;
        try {
            model = new GsonBuilder().create().fromJson(
                    new InputStreamReader(context.getAssets().open("test.json")),
                    new TypeToken<BaseModel<ArrayList<CardEntity>>>() {}.getType()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model != null ? model.results : null;
    }
}
