package com.mehmethalman.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {
    private String productId;
    private Integer quantity;
}