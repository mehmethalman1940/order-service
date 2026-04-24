package com.mehmethalman.orderservice.repository;

import com.mehmethalman.orderservice.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}
