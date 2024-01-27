package com.sigma.caller;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KeywordItem{
    @SerializedName("keyword")
    private String keyword;

    @SerializedName("category")
    private String category;

    @SerializedName("allowed_for_host")
    private String allowedForHost; // Change the type accordingly

    // Add constructors, getters, and setters as needed

    public String getKeyword() {
        return keyword;
    }

    public String getCategory() {
        return category;
    }

    public String getAllowedForHost() {
        return allowedForHost;
    }
}

