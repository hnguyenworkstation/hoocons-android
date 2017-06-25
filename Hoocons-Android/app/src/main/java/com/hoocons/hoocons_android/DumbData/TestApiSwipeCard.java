package com.hoocons.hoocons_android.DumbData;

import com.hoocons.hoocons_android.CustomUI.swipe_cards.BaseModel;
import com.hoocons.hoocons_android.CustomUI.swipe_cards.CardEntity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hungnguyen on 6/25/17.
 */

public interface TestApiSwipeCard {
    String HOST = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/";

    @GET("10/{pageNo}")
    Call<BaseModel<ArrayList<CardEntity>>> getGirlList(@Path("pageNo") int pageIndex);
}
