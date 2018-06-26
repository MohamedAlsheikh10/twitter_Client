package com.itqanbs.twetter.view.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.itqanbs.twetter.Presenter.LoginPresenter;
import com.itqanbs.twetter.R;
import com.itqanbs.twetter.databinding.ActivityMainBinding;
import com.itqanbs.twetter.view.followers.UserFollowersActivity;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, MainActivityContract.TaskView {

    private static final String TAG = "TwitterLogin";
    ActivityMainBinding binding;
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    // [START declare_auth]

// [END declare_auth]

    private TwitterLoginButton mLoginButton;
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitPresenter();

        // Configure Twitter SDK
        presenter.ConfigureTwitterSDK();

        // Inflate layout (must be done after Twitter is configured)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonTwitterSignout.setOnClickListener(this);

        // Initialize Firebase Auth
        presenter.InitializeFirebaseAuth();

        // END initialize_auth

        // START initialize_twitter_login
        binding.buttonTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
               ShowMessage("twitterLogin:success" );
               presenter. handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                ShowMessage("twitterLogin:failure" );
                updateUI(null);
            }
        });

    }

    // START on_start_check_user
    @Override
    public void onStart() {
        super.onStart();
       presenter.OnStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.buttonTwitterLogin.onActivityResult(requestCode, resultCode, data);
    }

    private void signOut() {
        presenter.mAuth.signOut();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();

        updateUI(null);
    }

    @Override
    public void ShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUI(FirebaseUser user) {
        // Check if user is signed in (non-null) and update UI accordingly.
        if (user != null) {
            binding.status.setText(getString(R.string.twitter_status_fmt, user.getDisplayName()));
            binding.detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            Intent userFollowersIntent = new Intent(MainActivity.this, UserFollowersActivity.class);
            userFollowersIntent.putExtra("USER", user);
            startActivity(userFollowersIntent);
            finish();

        } else {
            binding.status.setText(R.string.signed_out);
            binding.detail.setText(null);
            binding.buttonTwitterLogin.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void InitPresenter( ) {
        presenter = new LoginPresenter();
        presenter.view = this;
        presenter.context = MainActivity.this;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_twitter_signout) {
            signOut();
        }
    }
}
