package com.gft.products.application.service;

import com.gft.products.application.exceptions.InvalidPriceCurrencyException;
import com.gft.products.application.exceptions.InvalidPriceDateRangeException;
import com.gft.products.application.exceptions.InvalidPriceValueException;
import com.gft.products.application.exceptions.PriceDateOverlapException;
import com.gft.products.application.exceptions.PriceNotFoundException;
import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.application.port.in.UpdateProductPricePort;
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
public class UpdateProductPriceService implements UpdateProductPricePort {

  private final ProductRepositoryPort productRepositoryPort;
  private final PriceRepositoryPort priceRepositoryPort;

  @Override
  public Price updateProductPrice(Long productId, Long priceId, BigDecimal value, String currency, LocalDate initDate, LocalDate endDate) {
    Objects.requireNonNull(productId, "productId must not be null");
    Objects.requireNonNull(priceId, "priceId must not be null");
    Objects.requireNonNull(value, "value must not be null");
    Objects.requireNonNull(initDate, "initDate must not be null");

    if (!productRepositoryPort.existsById(productId)) {
      throw new ProductNotFoundException(productId);
    }

    priceRepositoryPort.findByIdAndProductId(productId, priceId)
        .orElseThrow(() -> new PriceNotFoundException(productId, priceId));

    if (value.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidPriceValueException();
    }

    String normalizedCurrency = normalizeCurrency(currency);

    if (endDate != null && !initDate.isBefore(endDate)) {
      throw new InvalidPriceDateRangeException();
    }

    Price price = Price.builder()
        .id(priceId)
        .value(value)
        .currency(normalizedCurrency)
        .initDate(initDate)
        .endDate(endDate)
        .build();

    if (priceRepositoryPort.existsOverlappingPriceExcludingId(productId, price)) {
      throw new PriceDateOverlapException();
    }

    return priceRepositoryPort.save(productId, price);
  }

  private String normalizeCurrency(String currency) {
    if (currency == null || currency.isBlank()) {
      throw new InvalidPriceCurrencyException();
    }

    String normalizedCurrency = currency.trim().toUpperCase();
    if (!normalizedCurrency.matches("[A-Z]{3}")) {
      throw new InvalidPriceCurrencyException();
    }
    return normalizedCurrency;
  }
}
