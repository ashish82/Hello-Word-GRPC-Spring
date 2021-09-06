package com.ashish.grpc.exception;

public class ServiceException extends Exception{

    private final   ErrorCode code;


    public ServiceException(ErrorCode code) {
        super();
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }

    public ServiceException(Throwable cause, ErrorCode code) {
        super(cause);
        this.code = code;
    }

    public ErrorCode getCode() {
        return this.code;
    }

}
