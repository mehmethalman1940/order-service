package com.mehmethalman.orderservice.service;

import com.mehmethalman.orderservice.client.ProductCatalogClient;
import com.mehmethalman.orderservice.dto.CreateOrderRequest;
import com.mehmethalman.orderservice.dto.ProductDto;
import com.mehmethalman.orderservice.entities.OrderEntity;
import com.mehmethalman.orderservice.exception.ProductNotFoundException;
import com.mehmethalman.orderservice.exception.ProductServiceUnavailableException;
import com.mehmethalman.orderservice.repository.OrderRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductCatalogClient productCatalogClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Retryable(
            value = { FeignException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2) // 2 sn, sonra 4 sn bekle
    )

    public OrderEntity createOrder(CreateOrderRequest request){
        ProductDto product = productCatalogClient.getProductById(request.getProductId());
        if (product == null) {
            throw new ProductNotFoundException(request.getProductId());
        }
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setOrderId(UUID.randomUUID().toString());
        orderEntity.setProductId(request.getProductId());
        orderEntity.setQuantity(request.getQuantity());
        orderEntity.setTotalPrice(totalPrice);
        orderEntity.setStatus("CREATED");

        OrderEntity savedOrder = orderRepository.save(orderEntity);
        String mesaj = "Yeni sipariş oluşturuldu Sipariş id: " + savedOrder.getOrderId() +
                " | Ürün id: " + savedOrder.getProductId() +
                " | Ürün Adedi: " + savedOrder.getQuantity() +
                " | Toplam Tutar: " + savedOrder.getTotalPrice() + " TL";

        kafkaTemplate.send("order-events", mesaj);
        System.out.println("Kafka mesaj gönderildi: " + mesaj);


        return savedOrder;

    }
    @Recover
    public OrderEntity fallbackForCreateOrder(FeignException e, CreateOrderRequest request) {

        throw new ProductServiceUnavailableException("Sistemde anlık bir yoğunluk var siparişiniz alınamadı... Lütfen daha sonra tekrar deneyiniz...");
    }

}
