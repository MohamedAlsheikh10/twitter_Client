package com.itqanbs.twetter.view.FollowerDetails;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.itqanbs.twetter.ImageDialoge;
import com.itqanbs.twetter.MyApplication;
import com.itqanbs.twetter.Presenter.FollowersInfoPresenter;
import com.itqanbs.twetter.R;
import com.itqanbs.twetter.databinding.ActivityUserFollowersinfoBinding;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.List;

public class UserFollowerInfoActivity extends AppCompatActivity implements FollowersDetailsContract.TaskView{
    ImageView bgImage;
    ProgressBar followersProgress;
    TextView followersResultText;
    ImageView followersReload;
    FollowersInfoPresenter presenter;
    LinearLayout mainLayout;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ActivityUserFollowersinfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initViews();
        InitPresenter();

        if (savedInstanceState != null) {
            if (MyApplication.getTweetsList() != null && MyApplication.getTweetsList().size() > 0) {
                updateUI(MyApplication.getTweetsList());
            } else {
                loadTweets();
            }

        } else {
            loadTweets();
        }
    }

    @Override
    public void loadTweets() {
        if (MyApplication.isNetworkAvailable(this)) {
            presenter.loadTwitterFriends(getIntent().getStringExtra("ScreenName"), getIntent().getLongExtra("ID", 0));
        } else {
            if (presenter.loadTweetsForOfflineMode(getIntent().getStringExtra("ScreenName")) != null && presenter.loadTweetsForOfflineMode(getIntent().getStringExtra("ScreenName")).size() > 0) {
                updateUI(presenter.loadTweetsForOfflineMode(getIntent().getStringExtra("ScreenName")));
            }else
            onErroroccured(getResources().getString(R.string.NoInterntConnection));
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_followersinfo);
        mainLayout = findViewById(R.id.main_layout);
        bgImage = findViewById(R.id.background);
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
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressDialog(getIntent().getStringExtra("ProfilePictureUrl"));
            }
        });
        Glide.with(this).
                load(getIntent().getStringExtra("Profile_background_image_url"))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(bgImage);
        bgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressDialog(getIntent().getStringExtra("Profile_background_image_url"));
            }
        });

        followersResultText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isNetworkAvailable(UserFollowerInfoActivity.this)) {
                    presenter.loadTwitterFriends(getIntent().getStringExtra("ScreenName"), getIntent().getLongExtra("ID", 0));

                } else {
                    onErroroccured(getResources().getString(R.string.NoInterntConnection));
                    ShowMessage(getResources().getString(R.string.NoInterntConnection));
                }
            }
        });

        followersReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isNetworkAvailable(UserFollowerInfoActivity.this)) {
                    presenter.loadTwitterFriends(getIntent().getStringExtra("ScreenName"), getIntent().getLongExtra("ID", 0));

                } else {
                    onErroroccured(getResources().getString(R.string.NoInterntConnection));
                    ShowMessage(getResources().getString(R.string.NoInterntConnection));

                }
            }
        });

         mSwipeRefreshLayout =  findViewById(R.id.swiperefresh);



        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    binding.fab.setVisibility(View.GONE);
                } else if (isShow) {
                    isShow = false;
                    binding.fab.setVisibility(View.VISIBLE);

                }
            }
        });
    }
    private void openAddressDialog(String imagepath) {
        int width;
        int height;
        ImageDialoge policyTermsDialog = new ImageDialoge(this, imagepath);
        policyTermsDialog.show();
        policyTermsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT > 8) {
            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight() - 60;
        } else {
            Point point = new Point();
            manager.getDefaultDisplay().getSize(point);
            width = point.x;
            height = point.y - 60;
        }
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(policyTermsDialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        policyTermsDialog.getWindow().setAttributes(lp);
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
        MyApplication.setTweetsList(followersList);

        for (com.twitter.sdk.android.core.models.Tweet tweet : followersList) {
            final TweetView tweetView2 = new TweetView(this, tweet,
                    R.style.tw__TweetDarkWithActionsStyle);
            mainLayout.addView(tweetView2);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null && presenter.listTweetCall != null && presenter.listTweetCall.isExecuted()) {
            presenter.cancel = true;
            presenter.listTweetCall.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);

    }


}
