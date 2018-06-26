package com.itqanbs.twetter.Presenter;

import android.content.Context;

import com.itqanbs.twetter.MyTwitterApiClient;
import com.itqanbs.twetter.adapter.Tweet;
import com.itqanbs.twetter.model.Follower;
import com.itqanbs.twetter.model.FriendsResponseModel;
import com.itqanbs.twetter.view.FollowerDetails.FollowersDetailsContract;
import com.itqanbs.twetter.view.FollowerDetails.UserFollowerInfoActivity;
import com.itqanbs.twetter.view.followers.UserFollowersActivity;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersInfo implements FollowersDetailsContract.TaskPresenter {
    public UserFollowerInfoActivity view;
    public Context context;
    public TwitterSession twittersession;
    List<Follower> FollowersList;

    @Override
    public void loadTwitterFriends() {
        view.activateReaload();
        twittersession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twittersession);
        myTwitterApiClient.getCustomTwitterService().listTweetCall("Mukesh17m").enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {

            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {

            }
        });
    }

    public List<Follower> fetchResults(Response response) {
        FriendsResponseModel responseModel = (FriendsResponseModel) response.body();
        return responseModel.getResults();
    }
}

