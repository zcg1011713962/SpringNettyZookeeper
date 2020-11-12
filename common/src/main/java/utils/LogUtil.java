package utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void info(String describe, Object... log) {
        logger.info(describe,log);
    }

    public static void info(String log) {
        logger.info(log);
    }

    public static void error(String describe, Object... log) {
        logger.error(describe, log);
    }
}
