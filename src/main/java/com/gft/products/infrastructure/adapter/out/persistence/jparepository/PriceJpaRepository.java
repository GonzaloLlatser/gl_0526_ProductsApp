package com.gft.products.infrastructure.adapter.out.persistence.jparepository;

import com.gft.products.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

  List<PriceEntity> findByProductIdOrderByInitDateAsc(Long productId);

  @Query("""
      SELECT price
      FROM PriceEntity price
      WHERE price.product.id = :productId
        AND price.initDate <= :date
        AND (price.endDate IS NULL OR price.endDate >= :date)
      ORDER BY price.initDate DESC
      """)
  Optional<PriceEntity> findActiveByProductIdAndDate(
      @Param("productId") Long productId,
      @Param("date") LocalDate date
  );

  @Query("""
      SELECT COUNT(price) > 0
      FROM PriceEntity price
      WHERE price.product.id = :productId
        AND price.initDate <= :endDate
        AND (price.endDate IS NULL OR price.endDate >= :initDate)
      """)
  boolean existsOverlappingPrice(
      @Param("productId") Long productId,
      @Param("initDate") LocalDate initDate,
      @Param("endDate") LocalDate endDate
  );
}
