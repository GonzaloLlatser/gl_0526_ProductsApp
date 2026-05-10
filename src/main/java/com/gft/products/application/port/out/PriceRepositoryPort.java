package com.gft.products.application.port.out;

import com.gft.products.domain.model.Price;
import com.gft.products.domain.model.PricePage;

import java.time.LocalDate;
import java.util.Optional;

public interface PriceRepositoryPort {

  Price save(Long productId, Price price);

  void deleteById(Long priceId);

  PricePage findByProductId(Long productId, int page, int size, String sortField, boolean ascending);

  Optional<Price> findByIdAndProductId(Long productId, Long priceId);

  Optional<Price> findActiveByProductIdAndDate(Long productId, LocalDate date);

  boolean existsOverlappingPrice(Long productId, Price price);

  boolean existsOverlappingPriceExcludingId(Long productId, Price price);
}
