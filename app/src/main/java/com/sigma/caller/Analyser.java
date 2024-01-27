package com.sigma.caller;

import java.util.HashMap;
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

    public Result check(String from, String content) {
        CheckData.CheckDataInfo el = data.get(from);

        if (Objects.isNull(el) || !el.block) {
            return new Result(true, el);
        }

        return new Result(false, el);
    }

    public void load() {
        data = new HashMap<>();

        data.put(
            "yandex.ru",
            new CheckData.CheckDataInfo(
                    "info",
                    "OK",
                    "OK",
                    false)
        );

        data.put(
                "998996099335",
                new CheckData.CheckDataInfo(
                        "warning",
                        "Предупреждение",
                        "Будьте аккуратны, сайт содержит недостоверный материал",
                        false
                )
        );

        data.put(
                "Payme",
                new CheckData.CheckDataInfo(
                        "critical",
                        "Стоп мошенник",
                        "Доступ к контенту ограничен. Так как содержит опасный контент!",
                        true
                )
        );
    }
}
