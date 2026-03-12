package com.capgemini.order_service.client;

import com.capgemini.order_service.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", path = "/products")
public interface ProductClient {

    @GetMapping("/api/product/{id}")
    ProductResponseDto getProductById(@PathVariable("id") Long id);

}
