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

    Analyser() {
        load();
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

        return null;
    }

    public void addData(Map.Entry<String, ResponseItem> entry) {
        data.putIfAbsent(entry.getKey(), new CheckData.CheckDataInfo(
                entry.getValue().getLevel(),
                entry.getValue().getTitle(),
                entry.getValue().getMessage(),
                entry.getValue().getBlocked()
        ));
    }

    public void load() {
        data = new HashMap<>();

//        data.put(
//            "yandex.ru",
//            new CheckData.CheckDataInfo(
//                    "info",
//                    "OK",
//                    "OK",
//                    false)
//        );
//
//        data.put(
//                "998996099335",
//                new CheckData.CheckDataInfo(
//                        "warning",
//                        "Предупреждение",
//                        "Будьте аккуратны, никому не сообщайте данные своей карты, коды из СМС",
//
//                        false
//                )
//        );
//
//        data.put(
//                "998907183601",
//                new CheckData.CheckDataInfo(
//                        "critical",
//                        "Ўўў бляя",
//                        "Мошенник квотти",
//                        false
//                )
//        );
//
//        data.put(
//                "+998907183601",
//                new CheckData.CheckDataInfo(
//                        "critical",
//                        "Ўўў бляя",
//                        "Мошенник квотти",
//                        false
//                )
//        );
//
//        data.put(
//                "Payme",
//                new CheckData.CheckDataInfo(
//                        "critical",
//                        "Стоп мошенник",
//                        "Доступ к контенту ограничен. Так как содержит опасный контент!",
//                        true
//                )
//        );
//
//        data.put(
//                "fake.site/getmoney",
//                new CheckData.CheckDataInfo(
//                        "critical",
//                        "Стоп мошенник",
//                        "Это сообщение содержить вредоносную ссылку!",
//                        true
//                )
//        );
    }
}
