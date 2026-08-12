package com.example.ecommerce_backend.controller;

import com.example.ecommerce_backend.dto.ProductRequest;
import com.example.ecommerce_backend.entity.Product;
import com.example.ecommerce_backend.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 1. ADD A NEW PRODUCT (POST)
    @PostMapping
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());

        productRepository.save(product);

        return new ResponseEntity<>("Product added successfully!", HttpStatus.CREATED);
    }

    // 2. GET ALL PRODUCTS (GET)
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // 3. GET A SINGLE PRODUCT BY ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        
        if (optionalProduct.isEmpty()) {
            return new ResponseEntity<>("Error: Product not found!", HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(optionalProduct.get(), HttpStatus.OK);
    }

    // 4. DELETE A PRODUCT (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return new ResponseEntity<>("Error: Product not found!", HttpStatus.NOT_FOUND);
        }
        
        productRepository.deleteById(id);
        return new ResponseEntity<>("Product deleted successfully!", HttpStatus.OK);
    }
}