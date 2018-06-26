package com.itqanbs.twetter.adapter;


import com.google.gson.annotations.SerializedName;
import com.itqanbs.twetter.model.Follower;

public class Tweet {

    @SerializedName("created_at")
    private String DateCreated;

    @SerializedName("id")
    private String Id;

    @SerializedName("text")
    private String Text;

    @SerializedName("in_reply_to_status_id")
    private String InReplyToStatusId;

    @SerializedName("in_reply_to_user_id")
    private String InReplyToUserId;

    @SerializedName("in_reply_to_screen_name")
    private String InReplyToScreenName;

    @SerializedName("user")
    private Follower User;

    public String getDateCreated() {
        return DateCreated;
    }

    public String getId() {
        return Id;
    }

    public String getInReplyToScreenName() {
        return InReplyToScreenName;
    }

    public String getInReplyToStatusId() {
        return InReplyToStatusId;
    }

    public String getInReplyToUserId() {
        return InReplyToUserId;
    }

    public String getText() {
        return Text;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        InReplyToScreenName = inReplyToScreenName;
    }

    public void setInReplyToStatusId(String inReplyToStatusId) {
        InReplyToStatusId = inReplyToStatusId;
    }

    public void setInReplyToUserId(String inReplyToUserId) {
        InReplyToUserId = inReplyToUserId;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setUser(Follower user) {
        User = user;
    }

    public Follower getUser() {
        return User;
    }

    @Override
    public String toString(){
        return getText();
    }
}