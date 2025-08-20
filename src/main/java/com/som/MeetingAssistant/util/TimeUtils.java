package com.som.MeetingAssistant.util;

import java.time.*;
import java.util.*;

public final class TimeUtils {
    private TimeUtils() {}

    public static LocalDateTime atTime(LocalDate date, int hour, int minute) {
        return LocalDateTime.of(date, LocalTime.of(hour, minute));
    }

    public static List<LocalDate> nextBusinessDays(int count, ZoneId zone) {
        List<LocalDate> days = new ArrayList<>();
        LocalDate d = LocalDate.now(zone);
        while (days.size() < count) {
            d = d.plusDays(1);
            DayOfWeek dow = d.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) days.add(d);
        }
        return days;
    }
}