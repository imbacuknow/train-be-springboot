package com.black.train.app.exception;

import lombok.Data;

@Data
public class DataNotFoundException extends RuntimeException {

    private String errorMsg;

    public DataNotFoundException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }
}
