package com.gft.products.application.service;

import com.gft.products.application.exceptions.PriceNotFoundException;
import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.application.port.in.GetCurrentProductPricePort;
import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GetCurrentProductPriceService implements GetCurrentProductPricePort {

  private final ProductRepositoryPort productRepositoryPort;
  private final PriceRepositoryPort priceRepositoryPort;

  @Override
  public Price getCurrentProductPrice(Long productId, LocalDate date) {
    Objects.requireNonNull(productId, "productId must not be null");
    Objects.requireNonNull(date, "date must not be null");

    if (!productRepositoryPort.existsById(productId)) {
      throw new ProductNotFoundException(productId);
    }

    return priceRepositoryPort.findActiveByProductIdAndDate(productId, date)
        .orElseThrow(() -> new PriceNotFoundException(productId, date));
  }
}
