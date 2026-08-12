package com.example.ecommerce_backend.controller;

import com.example.ecommerce_backend.dto.OrderRequest;
import com.example.ecommerce_backend.entity.Order;
import com.example.ecommerce_backend.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 1. PLACE A NEW ORDER
    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());

        orderRepository.save(order);

        return new ResponseEntity<>("Order placed successfully!", HttpStatus.CREATED);
    }

    // 2. GET ALL ORDERS FOR A USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}