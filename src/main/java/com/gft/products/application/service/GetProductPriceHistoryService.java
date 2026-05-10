package com.gft.products.application.service;

import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.application.port.in.GetProductPriceHistoryPort;
import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.PricePage;
import com.gft.products.domain.model.Product;
import com.gft.products.domain.model.ProductPriceHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GetProductPriceHistoryService implements GetProductPriceHistoryPort {

  private static final String DEFAULT_SORT = "initDate,asc";

  private final ProductRepositoryPort productRepositoryPort;
  private final PriceRepositoryPort priceRepositoryPort;

  @Override
  public ProductPriceHistory getProductPriceHistory(Long productId, int page, int size, String sort) {
    Objects.requireNonNull(productId, "productId must not be null");

    Product product = productRepositoryPort.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    String[] sortParts = resolveSort(sort);
    PricePage pricePage = priceRepositoryPort.findByProductId(
        productId,
        page,
        size,
        sortParts[0],
        "asc".equalsIgnoreCase(sortParts[1]));

    return ProductPriceHistory.builder()
        .name(product.getName())
        .description(product.getDescription())
        .prices(pricePage.getPrices())
        .page(pricePage.getPage())
        .size(pricePage.getSize())
        .totalElements(pricePage.getTotalElements())
        .totalPages(pricePage.getTotalPages())
        .build();
  }

  private String[] resolveSort(String sort) {
    String resolvedSort = sort == null || sort.isBlank() ? DEFAULT_SORT : sort;
    return resolvedSort.split(",");
  }
}
