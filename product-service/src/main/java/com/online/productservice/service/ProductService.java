package com.online.productservice.service;

import com.online.productservice.dto.ProductRequest;
import com.online.productservice.model.Product;
import com.online.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public void createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();
        repository.save(product);
        log.info("Product saved successfully: {}", product.getId());
    }

    public List<Product> getAllProducts(){
        return repository.findAll();
    }
}
