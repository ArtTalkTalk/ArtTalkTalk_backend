package org.example.youth_be.common.enums;


import org.slf4j.Logger;

public enum LogLevel {
    DEBUG {
        public void log(Logger logger, String message) {
            if (message != null) {
                logger.debug(message);
            }
        }
    },
    INFO {
        public void log(Logger logger, String message) {
            if (message != null) {
                logger.info(message);
            }
        }
    },

    WARN {
        public void log(Logger logger, String message) {
            if (message != null) {
                logger.warn(message);
            }
        }
    },

    ERROR {
        public void log(Logger logger, String message) {
            if (message != null) {
                logger.error(message);
            }
        }
    };
    public void log(Logger logger, String message) {}
}
