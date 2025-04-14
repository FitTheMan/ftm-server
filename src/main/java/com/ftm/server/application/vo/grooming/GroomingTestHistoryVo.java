package com.ftm.server.application.vo.grooming;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Getter;

@Getter
public class GroomingTestHistoryVo {

    private final List<String> historyDates;

    private GroomingTestHistoryVo(List<String> historyDates) {
        this.historyDates = historyDates;
    }

    public static GroomingTestHistoryVo from(List<LocalDateTime> historyDates) {
        return new GroomingTestHistoryVo(
                historyDates.stream()
                        .map(LocalDateTime::toLocalDate)
                        .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .toList());
    }
}
