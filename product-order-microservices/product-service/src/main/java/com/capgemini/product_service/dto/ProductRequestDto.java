package com.capgemini.product_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRequestDto {
	
    private Long productId;

    private String productName;
    private String productDescription;
    private Double productPrice;

}