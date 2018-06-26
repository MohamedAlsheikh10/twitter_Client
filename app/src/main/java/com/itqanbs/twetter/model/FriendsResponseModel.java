package com.itqanbs.twetter.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Altaf on 28-Oct-17.
 */

public class FriendsResponseModel {
    @SerializedName("previous_cursor")
    @Expose
    private Integer previousCursor;

    @SerializedName("previous_cursor_str")
    @Expose
    private String previousCursorStr;

    @SerializedName("next_cursor_str")
    @Expose
    private String nextCursorStr;

    @SerializedName("users")
    @Expose
    private List<Follower> results = new ArrayList<Follower>();

    public Integer getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(Integer previousCursor) {
        this.previousCursor = previousCursor;
    }

    public String getPreviousCursorStr() {
        return previousCursorStr;
    }

    public void setPreviousCursorStr(String previousCursorStr) {
        this.previousCursorStr = previousCursorStr;
    }

    public String getNextCursorStr() {
        return nextCursorStr;
    }

    public void setNextCursorStr(String nextCursorStr) {
        this.nextCursorStr = nextCursorStr;
    }

    public List<Follower> getResults() {
        return results;
    }

    public void setResults(List<Follower> results) {
        this.results = results;
    }
}