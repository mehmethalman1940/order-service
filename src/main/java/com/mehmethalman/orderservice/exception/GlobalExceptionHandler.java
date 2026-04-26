package com.mehmethalman.orderservice.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(ProductNotFoundException ex, Locale locale) {


        String errorMessage = messageSource.getMessage(
                "exception.product.notfound",
                new Object[]{ex.getProductId()},
                locale
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}