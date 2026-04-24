package com.mehmethalman.orderservice.client;

import com.mehmethalman.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-catalog-service", url = "http://localhost:8080")

public interface ProductCatalogClient {

    @GetMapping("/api/products/{id}")
    ProductDto getProductById(@PathVariable("id") String id);
}
