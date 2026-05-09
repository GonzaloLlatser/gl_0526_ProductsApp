package com.gft.products.application.service;

import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.application.port.in.GetProductPriceHistoryPort;
import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GetProductPriceHistoryService implements GetProductPriceHistoryPort {

  private final ProductRepositoryPort productRepositoryPort;
  private final PriceRepositoryPort priceRepositoryPort;

  @Override
  public Product getProductPriceHistory(Long productId) {
    Objects.requireNonNull(productId, "productId must not be null");

    Product product = productRepositoryPort.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));

    return Product.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .prices(priceRepositoryPort.findByProductId(productId))
        .build();
  }
}
