package com.itqanbs.twetter.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.itqanbs.twetter.view.login.MainActivityContract;
import com.itqanbs.twetter.R;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterSession;

public class LoginPresenter implements MainActivityContract.TaskPresenter {
    public MainActivityContract.TaskView view;
    public Context context;
    public FirebaseAuth mAuth;


    @Override
    public void ConfigureTwitterSDK() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                context.getString(R.string.twitter_consumer_key),
                context.getString(R.string.twitter_consumer_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(context)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);
    }

    @Override
    public void handleTwitterSession(TwitterSession session) {

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            view.updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            view.ShowMessage("Authentication failed.");
                            view.updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void InitializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void OnStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        view.updateUI(currentUser);
    }
}
