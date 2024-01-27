package com.sigma.caller;

import java.util.HashMap;
import java.util.Map;

public class CheckData {
    public static class CheckDataInfo {
        public static final String LEVEL_WARNING = "warning";

        public String level;
        public String title;
        public String message;
        public boolean block;

        public CheckDataInfo(String level, String title, String message, boolean block) {
            this.level = level;
            this.title = title;
            this.message = message;
            this.block = block;
        }

        // Getters and setters for the properties
    }
}

