package com.itqanbs.twetter.view.FollowerDetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.itqanbs.twetter.MyApplication;
import com.itqanbs.twetter.Presenter.FollowersInfoPresenter;
import com.itqanbs.twetter.R;
import com.itqanbs.twetter.databinding.ActivityUserFollowersinfoBinding;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.List;

public class UserFollowerInfoActivity extends AppCompatActivity implements FollowersDetailsContract.TaskView {

    ProgressBar followersProgress;
    TextView followersResultText;
    ImageView followersReload;
    FollowersInfoPresenter presenter;
    LinearLayout mainLayout;


    ActivityUserFollowersinfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_followersinfo);
        initViews();
        InitPresenter();
        if (MyApplication.isNetworkAvailable(this)) {
            presenter.loadTwitterFriends(getIntent().getStringExtra("ScreenName"), getIntent().getLongExtra("ID", 0));
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
        presenter = new FollowersInfoPresenter();
        presenter.view = this;
        presenter.context = UserFollowerInfoActivity.this;
    }

    @Override
    public void initViews() {


        mainLayout = findViewById(R.id.main_layout);
        ImageView bgImage = findViewById(R.id.background);
        followersProgress = findViewById(R.id.followers_progress);
        followersResultText = findViewById(R.id.followers_result_text);
        followersReload = findViewById(R.id.followers_reload);
        binding.toolbar.setTitle(getIntent().getStringExtra("Name"));
        setSupportActionBar(binding.toolbar);

        Glide.with(this).
                load(getIntent().getStringExtra("ProfilePictureUrl"))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_account_box_black_24dp))
                .into(binding.fab);

        Glide.with(this).
                load(getIntent().getStringExtra("Profile_background_image_url"))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(bgImage);


        followersResultText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isNetworkAvailable(UserFollowerInfoActivity.this)) {
                    presenter.loadTwitterFriends(getIntent().getStringExtra("ScreenName"), getIntent().getLongExtra("ID", 0));

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
                    presenter.loadTwitterFriends(getIntent().getStringExtra("ScreenName"), getIntent().getLongExtra("ID", 0));

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
    public void updateUI(List<com.twitter.sdk.android.core.models.Tweet> followersList) {
        followersProgress.setVisibility(View.INVISIBLE);
        followersResultText.setVisibility(View.INVISIBLE);
        followersReload.setVisibility(View.INVISIBLE);


        for (com.twitter.sdk.android.core.models.Tweet tweet : followersList) {
            final TweetView tweetView2 = new TweetView(this, tweet,
                    R.style.tw__TweetDarkWithActionsStyle);
            mainLayout.addView(tweetView2);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter!=null&&presenter.listTweetCall.isExecuted()) {
            presenter.cancel=true;
            presenter.listTweetCall.cancel();
        }
    }
}
