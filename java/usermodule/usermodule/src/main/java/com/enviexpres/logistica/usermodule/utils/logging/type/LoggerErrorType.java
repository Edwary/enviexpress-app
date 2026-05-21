package com.enviexpres.logistica.usermodule.utils.logging.type;

public enum LoggerErrorType {

    NO_CONTROLED ("No controlado", 500);

    private final String message;
    private final int code;

    private LoggerErrorType(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}

