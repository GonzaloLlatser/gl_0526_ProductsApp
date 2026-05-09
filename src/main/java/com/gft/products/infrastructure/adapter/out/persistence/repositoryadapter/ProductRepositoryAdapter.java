package com.gft.products.infrastructure.adapter.out.persistence.repositoryadapter;

import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Price;
import com.gft.products.domain.model.Product;
import com.gft.products.infrastructure.adapter.out.persistence.entity.PriceEntity;
import com.gft.products.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.gft.products.infrastructure.adapter.out.persistence.jparepository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

  private final ProductJpaRepository productJpaRepository;

  @Override
  @Transactional
  public Product save(Product product) {
    ProductEntity savedProduct = productJpaRepository.save(toEntity(product));
    return toDomain(savedProduct);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Product> findById(Long productId) {
    return productJpaRepository.findById(productId)
        .map(this::toDomain);
  }

  @Override
  public boolean existsById(Long productId) {
    return productJpaRepository.existsById(productId);
  }

  private ProductEntity toEntity(Product product) {
    return ProductEntity.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .build();
  }

  private Product toDomain(ProductEntity productEntity) {
    return Product.builder()
        .id(productEntity.getId())
        .name(productEntity.getName())
        .description(productEntity.getDescription())
        .prices(toPrices(productEntity.getPrices()))
        .build();
  }

  private List<Price> toPrices(List<PriceEntity> priceEntities) {
    return priceEntities.stream()
        .map(this::toPrice)
        .toList();
  }

  private Price toPrice(PriceEntity priceEntity) {
    return Price.builder()
        .id(priceEntity.getId())
        .value(priceEntity.getValue())
        .initDate(priceEntity.getInitDate())
        .endDate(priceEntity.getEndDate())
        .build();
  }
}
