package com.longingfuture.parval.exception;

public class BeanParseException extends Exception {

    private static final long serialVersionUID = 1L;

    private Integer errorCode;
    private String message;

    public BeanParseException(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public BeanParseException(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BeanParseException(Integer errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BeanParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanParseException(Throwable cause) {
        super(cause);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
