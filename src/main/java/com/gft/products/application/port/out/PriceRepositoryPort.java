package com.gft.products.application.port.out;

import com.gft.products.domain.model.Price;
import com.gft.products.domain.model.PricePage;

import java.time.LocalDate;
import java.util.Optional;

public interface PriceRepositoryPort {

  Price save(Long productId, Price price);

  PricePage findByProductId(Long productId, int page, int size, String sortField, boolean ascending);

  Optional<Price> findActiveByProductIdAndDate(Long productId, LocalDate date);

  boolean existsOverlappingPrice(Long productId, Price price);
}
