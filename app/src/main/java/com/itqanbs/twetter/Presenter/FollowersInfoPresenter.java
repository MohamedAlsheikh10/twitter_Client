package com.itqanbs.twetter.Presenter;

import android.content.Context;

import com.itqanbs.twetter.MyTwitterApiClient;
import com.itqanbs.twetter.model.Tweet;
import com.itqanbs.twetter.model.Follower;
import com.itqanbs.twetter.view.FollowerDetails.FollowersDetailsContract;
import com.itqanbs.twetter.view.FollowerDetails.UserFollowerInfoActivity;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersInfoPresenter implements FollowersDetailsContract.TaskPresenter {
    public UserFollowerInfoActivity view;
    public Context context;
    public TwitterSession twittersession;
    List<com.twitter.sdk.android.core.models.Tweet> FollowersList;
    public Call<List<com.twitter.sdk.android.core.models.Tweet>> listTweetCall;
    public boolean cancel=false;
    @Override
    public void loadTwitterFriends(String name,Long ID) {
        view.activateReaload();
        twittersession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twittersession);
        listTweetCall= myTwitterApiClient.getCustomTwitterService().listTweetCall(name,ID);
        listTweetCall.enqueue(new Callback<List<com.twitter.sdk.android.core.models.Tweet>>() {
            @Override
            public void onResponse(Call<List<com.twitter.sdk.android.core.models.Tweet>> call, Response<List<com.twitter.sdk.android.core.models.Tweet>> response) {
                FollowersList = response.body();
                view.updateUI(FollowersList);
            }

            @Override
            public void onFailure(Call<List<com.twitter.sdk.android.core.models.Tweet>> call, Throwable t) {
                if(!cancel) {
                    view.ShowMessage("Error in connection");
                    view.onErroroccured("Error in connection");
                }
            }
        });
    }

    @Override
    public List<Follower> fetchResults(Response response) {
        return null;
    }


}

