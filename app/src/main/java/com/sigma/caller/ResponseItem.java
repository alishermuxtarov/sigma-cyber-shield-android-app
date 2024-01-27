package com.sigma.caller;

import com.google.gson.annotations.SerializedName;

public class ResponseItem {
    @SerializedName("level")
    private String level;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("block")
    private boolean isBlocked;

    // Getter methods

    public String getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean getBlocked() {
        return isBlocked;
    }

}
