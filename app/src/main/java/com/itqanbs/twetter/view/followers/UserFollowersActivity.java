package com.itqanbs.twetter.view.followers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.itqanbs.twetter.MyApplication;
import com.itqanbs.twetter.Presenter.FollowersPresenter;
import com.itqanbs.twetter.Presenter.LoginPresenter;
import com.itqanbs.twetter.R;
import com.itqanbs.twetter.adapter.FollowersAdapter;
import com.itqanbs.twetter.databinding.ActivityUserFollowersBinding;
import com.itqanbs.twetter.model.Follower;
import com.itqanbs.twetter.view.login.MainActivity;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;
import java.util.Locale;

public class UserFollowersActivity extends AppCompatActivity implements FollowersContract.TaskView, SwipeRefreshLayout.OnRefreshListener {
    public TwitterSession twittersession;
    FollowersPresenter presenter;
    ActivityUserFollowersBinding binding;
    FollowersAdapter followersAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

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
            } else {
                onErroroccured(getResources().getString(R.string.NoInterntConnection));
                ShowMessage(getResources().getString(R.string.NoInterntConnection));
            }
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
        toolbar.setTitle(getResources().getString(R.string.title_activity_user_followers));
        setSupportActionBar(toolbar);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);


        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.followerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else {
            binding.followerRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }


        binding.followerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.followersResultText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isNetworkAvailable(UserFollowersActivity.this)) {
                    presenter.loadTwitterFriends();
                } else {
                    onErroroccured(getResources().getString(R.string.NoInterntConnection));
                    ShowMessage(getResources().getString(R.string.NoInterntConnection));
                }
            }
        });

        binding.followersReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isNetworkAvailable(UserFollowersActivity.this)) {
                    presenter.loadTwitterFriends();
                } else {
                    onErroroccured(getResources().getString(R.string.NoInterntConnection));
                    ShowMessage(getResources().getString(R.string.NoInterntConnection));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_followers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            showAlert();
        }

        if (id == R.id.action_arabic) {

            MyApplication.FOLLOWERS_session_editor.putString("LANG", "ar");
            MyApplication.FOLLOWERS_session_editor.commit();
            changeLang("ar");
        }
        if (id == R.id.action_EN) {
            MyApplication.FOLLOWERS_session_editor.putString("LANG", "en");
            MyApplication.FOLLOWERS_session_editor.commit();
            changeLang("en");
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Locale myLocale = new Locale(lang);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(myLocale);
        } else {
            configuration.locale = myLocale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
        finish();
        startActivity(getIntent());
    }

    private void signOut() {
        LoginPresenter.mAuth.signOut();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        MyApplication.FOLLOWERS_session_editor.clear();
        MyApplication.FOLLOWERS_session_editor.commit();
        Intent intent = new Intent(UserFollowersActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    public void onRefresh() {
        binding.followersProgress.setVisibility(View.VISIBLE);
        followersAdapter.clear();
        loadFollowers();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    void showAlert() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("LOGOUT")
                .setMessage("Are you sure you want to LOGOUT?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
