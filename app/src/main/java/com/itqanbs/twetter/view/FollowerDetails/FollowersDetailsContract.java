package com.itqanbs.twetter.view.FollowerDetails;

import java.util.List;

public interface FollowersDetailsContract {
    public interface TaskView {
        public void ShowMessage(String message);

        public void InitPresenter();

        public void initViews();

        public void onErroroccured(String message);

        public void activateReaload();
        public void loadTweets();

        public void updateUI(List<com.twitter.sdk.android.core.models.Tweet> followersList);


    }

    public interface TaskPresenter {
        public void loadTwitterFriends(String name,Long ID);

        public void saveFollowersForOfflineMode(List<com.twitter.sdk.android.core.models.Tweet> FollowersList, String name);
        public List<com.twitter.sdk.android.core.models.Tweet> loadTweetsForOfflineMode(String name );


    }

    public interface TaskModel {

    }
}
