package com.itqanbs.twetter.view.followers;

import com.itqanbs.twetter.model.Follower;

import java.util.List;

import retrofit2.Response;

public interface FollowersContract {
    public interface TaskView {
        public void ShowMessage(String message);

        public void InitPresenter();

        public void initViews();

        public void onErroroccured(String message);

        public void activateReaload();
        public void updateUI(List<Follower> followersList);
    }

    public interface TaskPresenter {
        public void loadTwitterFriends();
        public void saveFollowersForOfflineMode( List<Follower> FollowersList);
        public List<Follower>  loadFollowersForOfflineMode( );
        public List<Follower> fetchResults(Response response);
    }

    public interface TaskModel {

    }
}
