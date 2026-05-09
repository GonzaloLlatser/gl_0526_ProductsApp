package com.gft.products.infrastructure.adapter.out.persistence.repositoryadapter;

import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.domain.model.Price;
import com.gft.products.infrastructure.adapter.out.persistence.entity.PriceEntity;
import com.gft.products.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.gft.products.infrastructure.adapter.out.persistence.jparepository.PriceJpaRepository;
import com.gft.products.infrastructure.adapter.out.persistence.jparepository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

  private final PriceJpaRepository priceJpaRepository;
  private final ProductJpaRepository productJpaRepository;

  @Override
  @Transactional
  public Price save(Long productId, Price price) {
    ProductEntity productEntity = productJpaRepository.getReferenceById(productId);
    PriceEntity savedPrice = priceJpaRepository.save(toEntity(price, productEntity));
    return toDomain(savedPrice);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Price> findByProductId(Long productId) {
    return priceJpaRepository.findByProductIdOrderByInitDateAsc(productId).stream()
        .map(this::toDomain)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Price> findActiveByProductIdAndDate(Long productId, LocalDate date) {
    return priceJpaRepository.findActiveByProductIdAndDate(productId, date)
        .map(this::toDomain);
  }

  @Override
  public boolean existsOverlappingPrice(Long productId, Price price) {
    LocalDate endDate = price.getEndDate() == null ? LocalDate.MAX : price.getEndDate();
    return priceJpaRepository.existsOverlappingPrice(productId, price.getInitDate(), endDate);
  }

  private PriceEntity toEntity(Price price, ProductEntity productEntity) {
    return PriceEntity.builder()
        .id(price.getId())
        .product(productEntity)
        .value(price.getValue())
        .initDate(price.getInitDate())
        .endDate(price.getEndDate())
        .build();
  }

  private Price toDomain(PriceEntity priceEntity) {
    return Price.builder()
        .id(priceEntity.getId())
        .value(priceEntity.getValue())
        .initDate(priceEntity.getInitDate())
        .endDate(priceEntity.getEndDate())
        .build();
  }
}
