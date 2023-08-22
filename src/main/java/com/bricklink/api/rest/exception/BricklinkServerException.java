package com.bricklink.api.rest.exception;

import feign.FeignException;

public class BricklinkServerException extends FeignException {
    public BricklinkServerException(int status, String message) {
        super(status, message);
    }
}
