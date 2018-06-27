package com.itqanbs.twetter.Presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itqanbs.twetter.MyApplication;
import com.itqanbs.twetter.MyTwitterApiClient;
import com.itqanbs.twetter.model.Follower;
import com.itqanbs.twetter.view.FollowerDetails.FollowersDetailsContract;
import com.itqanbs.twetter.view.FollowerDetails.UserFollowerInfoActivity;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersInfoPresenter implements FollowersDetailsContract.TaskPresenter {
    public UserFollowerInfoActivity view;
    public Context context;
    public TwitterSession twittersession;
    public List<com.twitter.sdk.android.core.models.Tweet> TweetssList;
    public Call<List<com.twitter.sdk.android.core.models.Tweet>> listTweetCall;
    public boolean cancel=false;
    Gson gson = new Gson();
    @Override
    public void loadTwitterFriends(final String name, Long ID) {
        view.activateReaload();
        twittersession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twittersession);
        listTweetCall= myTwitterApiClient.getCustomTwitterService().listTweetCall(name,ID);
        listTweetCall.enqueue(new Callback<List<com.twitter.sdk.android.core.models.Tweet>>() {
            @Override
            public void onResponse(Call<List<com.twitter.sdk.android.core.models.Tweet>> call, Response<List<com.twitter.sdk.android.core.models.Tweet>> response) {
                TweetssList = response.body();
                saveFollowersForOfflineMode(TweetssList,name);
                view.updateUI(TweetssList);
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
    public void saveFollowersForOfflineMode(List<Tweet> TweetsList, String name) {
        MyApplication.FOLLOWERS_session_editor.putString(name, gson.toJson(TweetsList));
        MyApplication.FOLLOWERS_session_editor.commit();
    }

    @Override
    public List<Tweet> loadTweetsForOfflineMode(String name) {
        List<Tweet> tweetsList = gson.fromJson(MyApplication.FOLLOWERS_session.getString(name, ""), new TypeToken<List<Tweet>>() {
        }.getType());

        return tweetsList;
    }


}

