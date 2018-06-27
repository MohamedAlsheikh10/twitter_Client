package com.itqanbs.twetter.view.followers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
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

        if (savedInstanceState != null) {
            if (MyApplication.getFollowersList() != null && MyApplication.getFollowersList().size() > 0) {
                updateUI(MyApplication.getFollowersList());
            } else {
                loadFollowers();
            }

        } else {
            loadFollowers();
        }
    }



    private void loadFollowers() {
        if (MyApplication.isNetworkAvailable(this)) {
            presenter.loadTwitterFriends();
        } else {
            if (presenter.loadFollowersForOfflineMode() != null && presenter.loadFollowersForOfflineMode().size() > 0) {
                updateUI(presenter.loadFollowersForOfflineMode());
            }else
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Followers");

        binding.followerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.followerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
    public void onBackPressed() {
        super.onBackPressed();
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
        MyApplication.setFollowersList(followersList);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter.list != null && presenter.list.isExecuted()) {
            presenter.cancel = true;
            presenter.list.cancel();
        }
    }

}
