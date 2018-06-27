package com.itqanbs.twetter.Presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itqanbs.twetter.MyApplication;
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
    List<Follower> followersList;
    public Call<FriendsResponseModel> list;
    public boolean cancel = false;
    Gson gson = new Gson();

    @Override
    public void loadTwitterFriends() {
        view.activateReaload();
        twittersession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twittersession);
        list = myTwitterApiClient.getCustomTwitterService().list(twittersession.getId());
        list.enqueue(new Callback<FriendsResponseModel>() {
            @Override
            public void onResponse(Call<FriendsResponseModel> call, Response<FriendsResponseModel> response) {

                followersList = fetchResults(response);
                saveFollowersForOfflineMode(followersList);
                view.updateUI(followersList);

            }

            @Override
            public void onFailure(Call<FriendsResponseModel> call, Throwable t) {
                if (!cancel) {
                    view.ShowMessage("Error in connection");
                    view.onErroroccured("Error in connection");
                }
            }
        });
    }

    public List<Follower> fetchResults(Response response) {
        FriendsResponseModel responseModel = (FriendsResponseModel) response.body();
        return responseModel.getResults();
    }

    public void saveFollowersForOfflineMode(List<Follower> FollowersList) {

        MyApplication.FOLLOWERS_session_editor.putString("Followers", gson.toJson(FollowersList));
        MyApplication.FOLLOWERS_session_editor.commit();

    }

    @Override
    public List<Follower> loadFollowersForOfflineMode() {
        List<Follower> FollowersList = gson.fromJson(MyApplication.FOLLOWERS_session.getString("Followers", ""), new TypeToken<List<Follower>>() {
        }.getType());

        return FollowersList;
    }
}
