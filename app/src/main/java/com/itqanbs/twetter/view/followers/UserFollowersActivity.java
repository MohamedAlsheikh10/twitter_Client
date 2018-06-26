package com.itqanbs.twetter.view.followers;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.itqanbs.twetter.model.Follower;
import com.itqanbs.twetter.R;
import com.itqanbs.twetter.databinding.ActivityUserFollowersBinding;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.List;

public class UserFollowersActivity extends AppCompatActivity implements FollowersContract.TaskView {
    List<Follower> FollowersList;
    public  TwitterSession twittersession;

    ActivityUserFollowersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this, R.layout.activity_user_followers);
        binding.followerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.followerRecyclerView.setLayoutManager(mLayoutManager);
        binding.followerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        FollowersList = new ArrayList();


    }

    @Override
    public void ShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
