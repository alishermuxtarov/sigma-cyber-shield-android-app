package com.sigma.caller;

import com.google.gson.annotations.SerializedName;

public class HostItem {
    @SerializedName("url")
    private String url;

    @SerializedName("category")
    private String category;

    @SerializedName("risk_level")
    private String riskLevel;

    // Add constructors, getters, and setters as needed

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getRiskLevel() {
        if (riskLevel.equals("suspicious")) {
            return "warning";
        }

        if (riskLevel.equals("blacklist")) {
            return "critical";
        }

        return "info";
    }
}
