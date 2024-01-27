package com.sigma.caller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static List<String> getUrls(String str) {
        Pattern pattern = Pattern.compile("\\b(?:https?://|www\\.)\\S+\\b|\\b\\S+\\.\\S+/\\S+\\b");
        Matcher matcher = pattern.matcher(str);

        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            String url = matcher.group();
            urls.add(url);
        }

        return urls;
    }

}
