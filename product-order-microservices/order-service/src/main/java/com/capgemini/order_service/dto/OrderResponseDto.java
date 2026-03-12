package com.capgemini.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long productId;
    private String productDescription;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
}
