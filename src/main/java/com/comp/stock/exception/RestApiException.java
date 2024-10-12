package com.comp.stock.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestApiException {
    private String errorMessage;
    private int statusCode;
}
