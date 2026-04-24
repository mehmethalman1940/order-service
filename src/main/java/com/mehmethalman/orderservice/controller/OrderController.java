package com.mehmethalman.orderservice.controller;

import com.mehmethalman.orderservice.dto.CreateOrderRequest;
import com.mehmethalman.orderservice.entities.OrderEntity;
import com.mehmethalman.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(@RequestBody CreateOrderRequest request){
        OrderEntity createdOrder = orderService.createOrder(request);
        return ResponseEntity.ok(createdOrder);
    }

}
