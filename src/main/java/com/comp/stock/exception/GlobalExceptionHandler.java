package com.comp.stock.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({IllegalStateException.class})
//    public ResponseEntity<RestApiException> illegalStateExceptionHandler(IllegalStateException ex) {
//        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
//        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
//    }
}
