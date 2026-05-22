package com.enviexpres.logistica.packmodule.utils.exception.type;

public enum ExceptionType {

	NO_COMPLETE_OPERATION("Sorry, try again", 1000), NO_DATA("No data", 600);
	
	private final String message;
	private final int code;
	
	private ExceptionType(String message, int code) {
		this.message = message;
		this.code = code;
	}
	
	public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public static ExceptionType getViSoftwareType(ExceptionType type) {
    	ExceptionType response=null;
    	ExceptionType[] array=ExceptionType.values();
        for (ExceptionType viSoftwareType : array) {
            if(viSoftwareType.equals(type)){
                response= viSoftwareType;
                break;
            }
        }
        return response;
    }
}
