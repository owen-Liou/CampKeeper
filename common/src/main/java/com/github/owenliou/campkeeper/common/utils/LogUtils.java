package com.github.owenliou.campkeeper.common.utils;

import com.github.owenliou.campkeeper.config.variables.Profiles;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.spi.LocationAwareLogger;

/**
 * 共用 Log 且可以追溯呼叫位置
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Slf4j
public class LogUtils {

    /**
     * 指定了 FQCN，顯示 log 時，就會略過
     * fully qualified class name
     */
    private static final String FQCN = LogUtils.class.getName();

    public static void logError(Class<?> fqcn, String format, Object... arguments) {
        log(fqcn.getName(), LocationAwareLogger.ERROR_INT, format, arguments);
    }

    public static void devLogWarn(String format, Object... arguments) {
        devLog(FQCN, LocationAwareLogger.WARN_INT, format, arguments);
    }

    public static void devLogWarn(Class<?> fqcn, String format, Object... arguments) {
        devLog(fqcn.getName(), LocationAwareLogger.WARN_INT, format, arguments);
    }

    public static void devLogInfo(String format, Object... arguments) {
        devLog(FQCN, LocationAwareLogger.INFO_INT, format, arguments);
    }

    public static void devLogInfo(Class<?> fqcn, String format, Object... arguments) {
        devLog(fqcn.getName(), LocationAwareLogger.INFO_INT, format, arguments);
    }

/*
    public static void devLogDebug(String format, Object... arguments) {
        devLog(FQCN, LocationAwareLogger.DEBUG_INT, format, arguments);
    }
*/

    /**
     * 只有在非正式環境才會顯示
     * @param format
     * @param arguments
     */
    private static void devLog(String fqcn, int level, String format, Object... arguments) {
        if (Profiles.isCurrentProfileMatch(Profiles.TEST, Profiles.DEV, Profiles.DEV_LAB, Profiles.LAB, Profiles.PROD)) {
            logWithLocationAwareLogger(fqcn, level, format, arguments);
        }
    }

    private static void log(String fqcn, int level, String format, Object... arguments) {
        logWithLocationAwareLogger(fqcn, level, format, arguments);
    }

    private static void logWithLocationAwareLogger(String fqcn, int level, String format, Object... arguments) {
//            log.warn(MessageFormatter.arrayFormat(format, arguments).getMessage()); // org.slf4j.helpers.MessageFormatter
        if (log instanceof LocationAwareLogger locationAwareLogger) {
            locationAwareLogger.log(null, fqcn, level, format, arguments, null);
        }
    }

}
