package com.itqanbs.twetter.view.followers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.itqanbs.twetter.MyApplication;
import com.itqanbs.twetter.Presenter.FollowersPresenter;
import com.itqanbs.twetter.R;
import com.itqanbs.twetter.adapter.FollowersAdapter;
import com.itqanbs.twetter.databinding.ActivityUserFollowersBinding;
import com.itqanbs.twetter.model.Follower;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

public class UserFollowersActivity extends AppCompatActivity implements FollowersContract.TaskView {
    public TwitterSession twittersession;
    FollowersPresenter presenter;
    ActivityUserFollowersBinding binding;
    FollowersAdapter followersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        InitPresenter();
        if (MyApplication.isNetworkAvailable(this)) {
            presenter.loadTwitterFriends();
        } else {
            onErroroccured("No Internet Connection");
        }
    }

    @Override
    public void ShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void InitPresenter() {
        presenter = new FollowersPresenter();
        presenter.view = this;
        presenter.context = UserFollowersActivity.this;
    }

    @Override
    public void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_followers);
        binding.followerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.followerRecyclerView.setLayoutManager(mLayoutManager);
        binding.followerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.followersResultText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isNetworkAvailable(UserFollowersActivity.this)) {
                    presenter.loadTwitterFriends();
                } else {
                    onErroroccured("No Internet Connection");
                    ShowMessage("No Internet Connection");
                }
            }
        });

        binding.followersReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isNetworkAvailable(UserFollowersActivity.this)) {
                    presenter.loadTwitterFriends();
                } else {
                    onErroroccured("No Internet Connection");
                    ShowMessage("No Internet Connection");

                }
            }
        });
    }

    @Override
    public void onErroroccured(String message) {
        binding.followersProgress.setVisibility(View.INVISIBLE);
        binding.followersResultText.setVisibility(View.VISIBLE);
        binding.followersReload.setVisibility(View.VISIBLE);
        binding.followersResultText.setText(message);
    }

    @Override
    public void activateReaload() {
        binding.followersProgress.setVisibility(View.VISIBLE);
        binding.followersResultText.setVisibility(View.INVISIBLE);
        binding.followersReload.setVisibility(View.INVISIBLE);
    }


    @Override
    public void updateUI(List<Follower> followersList) {
        binding.followersProgress.setVisibility(View.INVISIBLE);
        binding.followersResultText.setVisibility(View.INVISIBLE);
        binding.followersReload.setVisibility(View.INVISIBLE);
        followersAdapter = new FollowersAdapter(this, followersList);
        binding.followerRecyclerView.setAdapter(followersAdapter);


    }


}
