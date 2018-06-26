package com.itqanbs.twetter.Presenter;

import android.content.Context;

import com.itqanbs.twetter.MyTwitterApiClient;
import com.itqanbs.twetter.model.Follower;
import com.itqanbs.twetter.model.FriendsResponseModel;
import com.itqanbs.twetter.view.followers.FollowersContract;
import com.itqanbs.twetter.view.followers.UserFollowersActivity;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersPresenter implements FollowersContract.TaskPresenter {
    public UserFollowersActivity view;
    public Context context;
    public TwitterSession twittersession;
    List<Follower> FollowersList;

    @Override
    public void loadTwitterFriends() {
        view.activateReaload();
        twittersession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twittersession);
        myTwitterApiClient.getCustomTwitterService().list(twittersession.getId()).enqueue(new Callback<FriendsResponseModel>() {
            @Override
            public void onResponse(Call<FriendsResponseModel> call, Response<FriendsResponseModel> response) {

                FollowersList = fetchResults(response);
                view.updateUI(FollowersList);

            }

            @Override
            public void onFailure(Call<FriendsResponseModel> call, Throwable t) {
                view.ShowMessage("Error in connection");
                view.onErroroccured("Error in connection");
            }
        });
    }

    public List<Follower> fetchResults(Response response) {
        FriendsResponseModel responseModel = (FriendsResponseModel) response.body();
        return responseModel.getResults();
    }
}
