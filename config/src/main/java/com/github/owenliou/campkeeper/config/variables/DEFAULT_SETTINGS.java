package com.github.owenliou.campkeeper.config.variables;

import java.time.format.DateTimeFormatter;

public interface DEFAULT_SETTINGS {

    String DEFAULT_TIMEZONE = "Asia/Taipei";

    int DEFAULT_OFFSET = 8;

    /**
     * yyyy/MM/dd
     */
    String DATE_PATTERN_1 = "yyyy/MM/dd";

    /**
     * yyyy/MM/dd
     */
    DateTimeFormatter DATE_FORMATTER_1 = DateTimeFormatter.ofPattern(DATE_PATTERN_1);

    /**
     * yyyy-MM-dd
     */
    String DATE_PATTERN_2 = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd
     */
    DateTimeFormatter DATE_FORMATTER_2 = DateTimeFormatter.ofPattern(DATE_PATTERN_2);

    /**
     * yyyyMMdd
     */
    String DATE_PATTERN_3 = "yyyyMMdd";
    /**
     * yyyyMMdd
     */
    DateTimeFormatter DATE_FORMATTER_3 = DateTimeFormatter.ofPattern(DATE_PATTERN_3);

    /**
     * yyyy-MM
     */
    String DATE_PATTERN_4 = "yyyy-MM";
    /**
     * yyyy-MM
     */
    DateTimeFormatter DATE_FORMATTER_4 = DateTimeFormatter.ofPattern(DATE_PATTERN_4);

    /**
     * yyyy/MM/dd HH:mm:ss
     */
    String DATE_TIME_PATTERN_1 = "yyyy/MM/dd HH:mm:ss";
    /**
     * yyyy/MM/dd HH:mm:ss
     */
    DateTimeFormatter DATE_TIME_FORMATTER_1 = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_1);

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    String DATE_TIME_PATTERN_2 = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    DateTimeFormatter DATE_TIME_FORMATTER_2 = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_2);

    /**
     * HH:mm:ss
     */
    String TIME_PATTERN_1 = "HH:mm:ss";
    /**
     * HH:mm:ss
     */
    DateTimeFormatter TIME_FORMATTER_1 = DateTimeFormatter.ofPattern(TIME_PATTERN_1);

    /**
     * e：1~7：日~六
     */
    String WEEK_PATTERN_1 = "e"; // 1~7
//    String WEEK_PATTERN_1 = "u"; // 1~7：一~日
    /**
     * e：1~7：日~六
     */
    DateTimeFormatter WEEK_FORMATTER_1 = DateTimeFormatter.ofPattern(WEEK_PATTERN_1);
//    SimpleDateFormat WEEK_FORMATTER_1 = new SimpleDateFormat(WEEK_PATTERN_1);

    /**
     * 115年01月10日
     */
    String ROC_DATE_PATTERN_1 = "%03d年%02d月%02d日";

    /**
     * 115年01月10日 10:22
     */
    String ROC_DATE_TIME_PATTERN_1 = "%03d年%02d月%02d日 %02d:%02d";

}
