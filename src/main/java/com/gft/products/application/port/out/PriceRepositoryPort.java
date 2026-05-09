package com.gft.products.application.port.out;

import com.gft.products.domain.model.Price;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceRepositoryPort {

  Price save(Long productId, Price price);

  List<Price> findByProductId(Long productId);

  Optional<Price> findActiveByProductIdAndDate(Long productId, LocalDate date);

  boolean existsOverlappingPrice(Long productId, Price price);
}
