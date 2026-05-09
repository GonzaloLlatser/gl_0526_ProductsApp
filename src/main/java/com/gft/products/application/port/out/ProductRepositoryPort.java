package com.gft.products.application.port.out;

import com.gft.products.domain.model.Product;

import java.util.Optional;

public interface ProductRepositoryPort {

  Product save(Product product);

  Optional<Product> findById(Long productId);

  boolean existsById(Long productId);
}
