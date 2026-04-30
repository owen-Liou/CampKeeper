package com.github.owenliou.campkeeper.utils;

import com.github.owenliou.campkeeper.variables.DEFAULT_SETTINGS;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.Date;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    /**
     * Asia/Taipei
     */
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_SETTINGS.DEFAULT_TIMEZONE);

    /**
     * +08:00
     */
    public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.ofHours(DEFAULT_SETTINGS.DEFAULT_OFFSET);

    /**
     * 現在日期時間
     * @return 台北日期時間
     */
    public static Date nowInDate() {
        return Date.from(LocalDateTime.now().atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * 現在日期
     * @return 台北現在日期
     */
    public static LocalDate nowInLocalDate() {
        return OffsetDateTime.now(DEFAULT_ZONE_ID).toLocalDate();
    }

    /**
     * 現在日期時間
     * @return 台北日期時間
     */
    public static LocalDateTime nowInLocalDateTime() {
        return OffsetDateTime.now(DEFAULT_ZONE_ID).toLocalDateTime();
    }

/*
    public static long nowInSeconds() {
        return nowInDate().getTime() / 1000;
    }
*/

    /*
     * 毫秒轉 OffsetDateTime 台北時間
     * @param milliseconds
    public static OffsetDateTime msTtoOffsetDateTime(long milliseconds) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), DEFAULT_ZONE_ID);
    }
     */

    /**
     * 毫秒轉 LocalDateTime 台北時間
     * @param milliseconds
     */
    public static LocalDateTime toLocalDateTime(long milliseconds) {
//        return Instant.ofEpochMilli(milli).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return Instant.ofEpochMilli(milliseconds).atZone(DEFAULT_ZONE_ID).toLocalDateTime();
    }

    /*
     * 秒轉 OffsetDateTime 台北時間
     * @param seconds
    public static OffsetDateTime secToOffsetDateTime(long seconds) {
        return msTtoOffsetDateTime(seconds * 1000);
    }

    public static long localDateTimeToSeconds(LocalDateTime value) {
        return value.toEpochSecond(DEFAULT_ZONE_OFFSET);
    }

    public static long localDateToSeconds(LocalDate value) {
        return value.atStartOfDay().toEpochSecond(DEFAULT_ZONE_OFFSET);
    }
     */

    /**
     * LocalDateTime 轉毫秒
     * @param dateTime
     */
    public static long toMillis(LocalDateTime dateTime) {
//        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return dateTime.atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
    }

    /**
     * 115年01月10日
     */
    public static String ROCDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        int rocYear = date.getYear() - 1911;
        return DEFAULT_SETTINGS.ROC_DATE_PATTERN_1.formatted(rocYear, date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * 115年01月10日 10:22
     */
    public static String ROCDateTime(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        int rocYear = date.getYear() - 1911;
        return DEFAULT_SETTINGS.ROC_DATE_TIME_PATTERN_1.formatted(rocYear, date.getMonthValue(), date.getDayOfMonth(), date.getHour(), date.getMinute());
    }

}
