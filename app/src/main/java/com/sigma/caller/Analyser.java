package com.sigma.caller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Analyser {
    public class Result {
        public Boolean isOk;
        public CheckData.CheckDataInfo extra;
        public Result(Boolean isOk, CheckData.CheckDataInfo extra) {
            this.isOk = isOk;
            this.extra = extra;
        }

        public Result(Boolean isOk) {
            this.isOk = isOk;
        }
    }

    private HashMap<String, CheckData.CheckDataInfo> data;
    private HashMap<String, CheckData.CheckDataInfo> keywords;

    Analyser() {
        init();
    }

    public Result check(String from) {
        return checkWithContent(from, "");
    }

    public Result checkWithContent(String from, String content) {
        CheckData.CheckDataInfo el = data.get(from);

        if (Objects.isNull(el) || !el.block) {
            if (!content.isEmpty()) {
                Result contentEl = checkContent(content);
                if (contentEl != null) {
                    return contentEl;
                }

            }
            return new Result(true, el);
        }

        Result contentEl = checkContent(content);
        if (contentEl != null) {
            return contentEl;
        }

        return new Result(false, el);
    }

    private Result checkContent(String content)
    {
        for (String url: Utils.getUrls(content)) {
            CheckData.CheckDataInfo urlEl = data.get(url);

            if (Objects.nonNull(urlEl)) {
                return new Result(false, urlEl);
            }
        }

        for (Map.Entry<String, CheckData.CheckDataInfo> entry : keywords.entrySet()) {
            if (content.contains(entry.getKey())) {
                return new Result(false, entry.getValue());
            }
        }

        return null;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void addData(HostItem hostItem) {



        data.putIfAbsent(hostItem.getUrl(), new CheckData.CheckDataInfo(
                hostItem.getRiskLevel(),
                hostItem.getRiskLevel().equals("warning") || (!hostItem.getRiskLevel().equals("critial") && isNumeric(hostItem.getUrl())) ? "Предупреждение" : "Тревога: Запрешенный URL",
                hostItem.getRiskLevel().equals("warning") || (!hostItem.getRiskLevel().equals("critial") && isNumeric(hostItem.getUrl()))
                    ? "Будьте бдительны, никому не сообщайте данные своей карты, коды из СМС"
                    : "Текст содержит опасный контент",
                hostItem.getRiskLevel() != "info"
        ));
    }

    public void addKeywords(KeywordItem keywordItem) {
        keywords.putIfAbsent(keywordItem.getKeyword(), new CheckData.CheckDataInfo(
                "critical",
                "Запрешенное ключевое слово",
                "Это сообщение содержить запрешенные данные или ссылку",
                true
        ));
    }

    public void init() {
        data = new HashMap<>();
        keywords = new HashMap<>();
    }
}
