package com.capgemini.product_service.service;

import com.capgemini.product_service.dto.ProductRequestDto;
import com.capgemini.product_service.dto.ProductResponseDto;
import com.capgemini.product_service.entity.Product;
import com.capgemini.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private ProductResponseDto mapToDto(Product product) {
        if (product == null) return null;
        return new ProductResponseDto(
                product.getProductId(),
                product.getProductName(),
                product.getProductDescription(),
                product.getProductPrice()
        );
    }

    public ProductResponseDto createProduct(ProductRequestDto dto) {
    	Product product = new Product();
    	product.setProductId(dto.getProductId());
    	product.setProductName(dto.getProductName());
    	product.setProductDescription(dto.getProductDescription());
    	product.setProductPrice(dto.getProductPrice());
        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(this::mapToDto).orElse(null);
    }
}
