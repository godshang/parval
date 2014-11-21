package com.longingfuture.parval.http;

public class HttpInvokeException extends RuntimeException {

    private static final long serialVersionUID = 4625095534811520018L;

    public HttpInvokeException() {
        super();
    }

    public HttpInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpInvokeException(String message) {
        super(message);
    }

    public HttpInvokeException(Throwable cause) {
        super(cause);
    }

}
