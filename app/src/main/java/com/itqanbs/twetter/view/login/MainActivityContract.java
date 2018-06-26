package com.itqanbs.twetter.view.login;

import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.TwitterSession;

public interface MainActivityContract {
    public interface TaskView{
        public void ShowMessage(String message);
        public void updateUI(FirebaseUser user);
        public void InitPresenter( );
    }

    public interface TaskPresenter {
        public void ConfigureTwitterSDK();
        public void handleTwitterSession(TwitterSession session);
        public void InitializeFirebaseAuth( );
        public void OnStart( );

    }   public interface TaskModel{

    }
}
