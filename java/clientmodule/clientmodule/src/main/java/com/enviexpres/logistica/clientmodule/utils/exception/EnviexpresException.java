package com.enviexpres.logistica.clientmodule.utils.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enviexpres.logistica.clientmodule.utils.exception.type.ExceptionType;
import com.enviexpres.logistica.clientmodule.utils.logging.type.LoggerErrorType;

public class EnviexpresException extends Exception {

	private static final Logger logger = LogManager.getLogger(EnviexpresException.class);

    private ExceptionType type;

    public EnviexpresException() {
        super();
        logger.error(LoggerErrorType.NO_CONTROLED);
    }

    public EnviexpresException(String message) {
        super(message);
        logger.error(message);
    }

    public EnviexpresException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message, cause);
    }

    public EnviexpresException(Throwable cause) {
        super(cause);
        logger.error(cause);
    }

    protected EnviexpresException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        logger.error(message, cause, enableSuppression, writableStackTrace);
    }

    public ExceptionType getType() {
        return type;
    }

    public void setType(ExceptionType type) {
        this.type = type;
    }
}
