package com.gft.products.infrastructure.adapter.out.persistence.repositoryadapter;

import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.domain.model.Price;
import com.gft.products.domain.model.PricePage;
import com.gft.products.infrastructure.adapter.out.persistence.entity.PriceEntity;
import com.gft.products.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.gft.products.infrastructure.adapter.out.persistence.jparepository.PriceJpaRepository;
import com.gft.products.infrastructure.adapter.out.persistence.jparepository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
  public PricePage findByProductId(Long productId, int page, int size, String sortField, boolean ascending) {
    Sort sort = ascending ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
    Page<PriceEntity> prices = priceJpaRepository.findByProductId(productId, PageRequest.of(page, size, sort));

    return PricePage.builder()
        .prices(prices.stream()
            .map(this::toDomain)
            .toList())
        .page(prices.getNumber())
        .size(prices.getSize())
        .totalElements(prices.getTotalElements())
        .totalPages(prices.getTotalPages())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Price> findActiveByProductIdAndDate(Long productId, LocalDate date) {
    return priceJpaRepository.findActiveByProductIdAndDate(productId, date)
        .map(this::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsOverlappingPrice(Long productId, Price price) {
    LocalDate endDate = price.getEndDate() == null ? LocalDate.MAX : price.getEndDate();
    return priceJpaRepository.existsOverlappingPrice(productId, price.getInitDate(), endDate);
  }

  private PriceEntity toEntity(Price price, ProductEntity productEntity) {
    return PriceEntity.builder()
        .id(price.getId())
        .product(productEntity)
        .value(price.getValue())
        .currency(price.getCurrency())
        .initDate(price.getInitDate())
        .endDate(price.getEndDate())
        .build();
  }

  private Price toDomain(PriceEntity priceEntity) {
    return Price.builder()
        .id(priceEntity.getId())
        .value(priceEntity.getValue())
        .currency(priceEntity.getCurrency())
        .initDate(priceEntity.getInitDate())
        .endDate(priceEntity.getEndDate())
        .build();
  }
}
