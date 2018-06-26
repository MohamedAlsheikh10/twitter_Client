package com.itqanbs.twetter;


import com.itqanbs.twetter.model.ServiceListeners;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by Altaf on 28-Oct-17.
 */

public class MyTwitterApiClient extends TwitterApiClient {

    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    /**
     * Provide CustomService with defined endpoints
     */
    public ServiceListeners getCustomTwitterService() {
        return getService(ServiceListeners.class);
    }
}