package com.itqanbs.twetter;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.itqanbs.twetter.model.Follower;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class MyApplication extends Application {
    public static boolean activityVisible;

    public static List<Tweet> getTweetsList() {
        return tweetsList;
    }

    public static void setTweetsList(List<Tweet> tweetsList) {
        MyApplication.tweetsList = tweetsList;
    }

    private static List<Tweet> tweetsList;

    public static List<Follower> getFollowersList() {
        return followersList;
    }

    public static void setFollowersList(List<Follower> followersList) {
        MyApplication.followersList = followersList;
    }

    private static List<Follower> followersList;
    public static SharedPreferences FOLLOWERS_session;
    public static SharedPreferences.Editor FOLLOWERS_session_editor;
    public static boolean isNetworkAvailable(Context activity) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
        FOLLOWERS_session = getSharedPreferences("FOLLOWERS_SESSION", MODE_PRIVATE);
        FOLLOWERS_session_editor = FOLLOWERS_session.edit();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

}
