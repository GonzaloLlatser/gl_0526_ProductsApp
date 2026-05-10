package com.gft.products.infrastructure.adapter.out.persistence.repositoryadapter;

import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Product;
import com.gft.products.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.gft.products.infrastructure.adapter.out.persistence.jparepository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional(readOnly = true)
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
        .build();
  }
}
