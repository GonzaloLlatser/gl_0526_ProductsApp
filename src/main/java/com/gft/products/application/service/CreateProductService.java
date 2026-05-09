package com.gft.products.application.service;

import com.gft.products.application.exceptions.InvalidProductException;
import com.gft.products.application.port.in.CreateProductPort;
import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductPort {

  private final ProductRepositoryPort productRepositoryPort;

  @Override
  public Product createProduct(String name, String description) {
    if (name == null || name.isBlank()) {
      throw new InvalidProductException();
    }

    Product product = Product.builder()
        .name(name.trim())
        .description(description)
        .build();

    return productRepositoryPort.save(product);
  }
}
