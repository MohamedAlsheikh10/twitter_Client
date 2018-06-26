package com.itqanbs.twetter.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Altaf on 28-Oct-17.
 */

public interface ServiceListeners {
    //For getting friends :  @GET("1.1/friends/list.json")
    @GET("1.1/followers/list.json")
    Call<FriendsResponseModel> list(@Query("user_id") long id);

    @GET("1.1/statuses/user_timeline.json")
    Call<List<com.twitter.sdk.android.core.models.Tweet>> listTweetCall(@Query("screen_name") String screen_name,@Query("user_id") Long id);
}