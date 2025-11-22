package com.universidad.compusearch.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
