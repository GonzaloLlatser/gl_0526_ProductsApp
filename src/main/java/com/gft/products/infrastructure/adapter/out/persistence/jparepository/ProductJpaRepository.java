package com.gft.products.infrastructure.adapter.out.persistence.jparepository;

import com.gft.products.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
