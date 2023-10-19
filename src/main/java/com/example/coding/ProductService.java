package com.example.coding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    private RestTemplate restTemplate;

    private void loadProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            ResponseEntity<Product[]> response = restTemplate.exchange(
                    "https://fakestoreapi.com/products",
                    HttpMethod.GET,
                    null,
                    Product[].class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Product> fetchedProducts = Arrays.asList(Objects.requireNonNull(response.getBody()));

                productRepository.saveAll(fetchedProducts);

            } else {
                throw new RuntimeException("Failed to fetch product data from fakestoreapi.");
            }
        }
    }

    public Page<Product> getProductsByFilters(BigDecimal minPrice, BigDecimal maxPrice, String category, int page, int pageSize) {
        loadProducts();
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return productRepository.findByFilters(minPrice, maxPrice, category, pageable);
    }


    public Optional<Product> getProductById(Long id) {
        loadProducts();
        return productRepository.findById(id);
    }
}