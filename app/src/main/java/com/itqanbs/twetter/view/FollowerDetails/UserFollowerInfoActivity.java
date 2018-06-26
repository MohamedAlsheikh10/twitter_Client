package com.itqanbs.twetter.view.FollowerDetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.itqanbs.twetter.MyApplication;
import com.itqanbs.twetter.Presenter.FollowersInfo;
import com.itqanbs.twetter.Presenter.FollowersPresenter;
import com.itqanbs.twetter.R;
import com.itqanbs.twetter.adapter.FollowersAdapter;
import com.itqanbs.twetter.databinding.ActivityUserFollowersBinding;
import com.itqanbs.twetter.databinding.ActivityUserFollowersinfoBinding;
import com.itqanbs.twetter.model.Follower;
import com.itqanbs.twetter.view.followers.UserFollowersActivity;

import java.util.List;

public class UserFollowerInfoActivity extends AppCompatActivity implements FollowersDetailsContract.TaskView {
    RecyclerView followertwitterRecyclerView;
ProgressBar followersProgress;
TextView followersResultText;
ImageView followersReload;
    FollowersInfo presenter;
    FollowersAdapter followersAdapter;
    ActivityUserFollowersinfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         binding=  DataBindingUtil.setContentView(this, R.layout.activity_user_followersinfo);
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
        presenter = new FollowersInfo();
        presenter.view = this;
        presenter.context = UserFollowerInfoActivity.this;
    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        followertwitterRecyclerView = findViewById(R.id.follower_recycler_view);
        followersProgress = findViewById(R.id.followers_progress);
        followersResultText = findViewById(R.id.followers_result_text);
        followersReload = findViewById(R.id.followers_reload);
        ImageView profilepic = findViewById(R.id.fab);
        toolbar.setTitle(getIntent().getStringExtra("Name"));
        setSupportActionBar(toolbar);

        Glide.with(this).
                load(getIntent().getStringExtra("ProfilePictureUrl"))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_account_box_black_24dp))
                .into(profilepic);


        followertwitterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        followertwitterRecyclerView.setLayoutManager(mLayoutManager);
        followertwitterRecyclerView.setItemAnimator(new DefaultItemAnimator());
        followersResultText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isNetworkAvailable(UserFollowerInfoActivity.this)) {
                    presenter.loadTwitterFriends();
                } else {
                    onErroroccured("No Internet Connection");
                    ShowMessage("No Internet Connection");
                }
            }
        });

        followersReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isNetworkAvailable(UserFollowerInfoActivity.this)) {
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
        followersProgress.setVisibility(View.INVISIBLE);
        followersResultText.setVisibility(View.VISIBLE);
        followersReload.setVisibility(View.VISIBLE);
        followersResultText.setText(message);
    }

    @Override
    public void activateReaload() {
        followersProgress.setVisibility(View.VISIBLE);
        followersResultText.setVisibility(View.INVISIBLE);
        followersReload.setVisibility(View.INVISIBLE);
    }


    @Override
    public void updateUI(List<Follower> followersList) {
        followersProgress.setVisibility(View.INVISIBLE);
        followersResultText.setVisibility(View.INVISIBLE);
        followersReload.setVisibility(View.INVISIBLE);
        followersAdapter = new FollowersAdapter(this, followersList);
        followertwitterRecyclerView.setAdapter(followersAdapter);


    }
}
