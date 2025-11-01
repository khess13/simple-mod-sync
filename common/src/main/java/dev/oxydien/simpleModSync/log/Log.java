package dev.oxydien.simpleModSync.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    private static Log instance;
    private static Logger logger;
    private static String modId;

    // Minimum log level - change this to control what gets logged
    private static final LogLevel MIN_LEVEL = LogLevel.ALL;

    private Log(String modId) {
        Log.modId = modId;
        logger = LoggerFactory.getLogger(modId);
    }

    /**
     * Initialize the logger with your mod ID
     * Call this once in your mod's initialization
     */
    public static void init(String modId) {
        if (instance == null) {
            instance = new Log(modId);
        }
    }

    /**
     * Log a debug message
     */
    public static void debug(Object... messages) {
        if (MIN_LEVEL.ordinal() <= LogLevel.DEBUG.ordinal()) {
            logger.info(formatMessage(messages));
        }
    }

    /**
     * Log an info message
     */
    public static void info(Object... messages) {
        if (MIN_LEVEL.ordinal() <= LogLevel.INFO.ordinal()) {
            logger.info(formatMessage(messages));
        }
    }

    /**
     * Log a warning message
     */
    public static void warning(Object... messages) {
        if (MIN_LEVEL.ordinal() <= LogLevel.WARNING.ordinal()) {
            logger.warn(formatMessage(messages));
        }
    }

    /**
     * Log an error message
     */
    public static void error(Object... messages) {
        if (MIN_LEVEL.ordinal() <= LogLevel.ERROR.ordinal()) {
            logger.error(formatMessage(messages));
        }
    }

    /**
     * Log an error message with an exception
     */
    public static void error(Throwable throwable, Object... messages) {
        if (MIN_LEVEL.ordinal() <= LogLevel.ERROR.ordinal()) {
            logger.error(formatMessage(messages), throwable);
        }
    }

    private static String formatMessage(Object... messages) {
        if (messages == null || messages.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(Log.modId);
        sb.append("] ");

        for (int i = 0; i < messages.length; i++) {
            sb.append(messages[i]);
            if (i < messages.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private enum LogLevel {
        ALL,
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }
}