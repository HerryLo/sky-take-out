package com.liuheng.exception;

public class DeletionNotAllowedException extends BaseException {
    public DeletionNotAllowedException(String message) {
        super(message);
    }
    public DeletionNotAllowedException(){};
}
