package com.capgemini.order_service.service;

import com.capgemini.order_service.client.ProductClient;
import com.capgemini.order_service.dto.OrderRequestDto;
import com.capgemini.order_service.dto.OrderResponseDto;
import com.capgemini.order_service.dto.ProductResponseDto;
import com.capgemini.order_service.entity.Order;
import com.capgemini.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderService(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    private OrderResponseDto mapToDto(Order order) {
        if (order == null) return null;
        return new OrderResponseDto(
                order.getOrderId(),
                order.getProductId(),
                order.getProductDescription(),
                order.getProductPrice(),
                order.getQuantity(),
                order.getProductPrice() != null && order.getQuantity() != null ? order.getProductPrice() * order.getQuantity() : 0.0
        );
    }

    public OrderResponseDto createOrder(OrderRequestDto request) {
        // Fetch product details from PRODUCT-SERVICE
        ProductResponseDto productResponse = productClient.getProductById(request.getProductId());

        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());

        if (productResponse != null) {
            order.setProductDescription(productResponse.getProductDescription());
            order.setProductPrice(productResponse.getProductPrice());
        }

        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderResponseDto getOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.map(this::mapToDto).orElse(null);
    }
}
