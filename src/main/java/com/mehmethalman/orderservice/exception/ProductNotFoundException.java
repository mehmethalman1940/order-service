package com.mehmethalman.orderservice.exception;


public class ProductNotFoundException extends RuntimeException {

    private final String productId;

    public ProductNotFoundException(String productId) {
        super(); // burada RuntimeExceptionun içine dışardan gelecek mesajı yollayacagızki fırlatabilelim
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}