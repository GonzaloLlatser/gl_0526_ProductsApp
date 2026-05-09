package com.gft.products.application.service;

import com.gft.products.application.exceptions.InvalidPriceDateRangeException;
import com.gft.products.application.exceptions.InvalidPriceValueException;
import com.gft.products.application.exceptions.PriceDateOverlapException;
import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.application.port.in.AddProductPricePort;
import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddProductPriceService implements AddProductPricePort {

  private final ProductRepositoryPort productRepositoryPort;
  private final PriceRepositoryPort priceRepositoryPort;

  @Override
  public Price addProductPrice(Long productId, BigDecimal value, LocalDate initDate, LocalDate endDate) {
    Objects.requireNonNull(productId, "productId must not be null");
    Objects.requireNonNull(value, "value must not be null");
    Objects.requireNonNull(initDate, "initDate must not be null");

    if (!productRepositoryPort.existsById(productId)) {
      throw new ProductNotFoundException(productId);
    }

    if (value.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidPriceValueException();
    }

    if (endDate != null && !initDate.isBefore(endDate)) {
      throw new InvalidPriceDateRangeException();
    }

    Price price = Price.builder()
        .value(value)
        .initDate(initDate)
        .endDate(endDate)
        .build();

    if (priceRepositoryPort.existsOverlappingPrice(productId, price)) {
      throw new PriceDateOverlapException();
    }

    return priceRepositoryPort.save(productId, price);
  }
}
