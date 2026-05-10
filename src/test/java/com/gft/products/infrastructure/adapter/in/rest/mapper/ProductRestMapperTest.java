package com.gft.products.infrastructure.adapter.in.rest.mapper;

import com.gft.products.domain.model.Price;
import com.gft.products.domain.model.Product;
import com.gft.products.domain.model.ProductPriceHistory;
import com.gft.products.infrastructure.adapter.in.rest.dto.CurrentPriceResponse;
import com.gft.products.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.gft.products.infrastructure.adapter.in.rest.dto.ProductPriceHistoryResponse;
import com.gft.products.infrastructure.adapter.in.rest.dto.ProductResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRestMapperTest {

  @Test
  void shouldMapProductToProductResponse() {
    Product product = Product.builder()
        .id(1L)
        .name("camiseta")
        .description("camiseta One love")
        .build();

    ProductResponse response = ProductRestMapper.toProductResponse(product);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("camiseta");
    assertThat(response.description()).isEqualTo("camiseta One love");
  }

  @Test
  void shouldMapPriceToPriceResponse() {
    Price price = Price.builder()
        .id(1L)
        .value(new BigDecimal("99.99"))
        .currency("EUR")
        .initDate(LocalDate.of(2024, 1, 1))
        .endDate(LocalDate.of(2024, 12, 31))
        .build();

    PriceResponse response = ProductRestMapper.toPriceResponse(price);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.value()).isEqualByComparingTo("99.99");
    assertThat(response.currency()).isEqualTo("EUR");
    assertThat(response.initDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    assertThat(response.endDate()).isEqualTo(LocalDate.of(2024, 12, 31));
  }

  @Test
  void shouldMapCurrentPriceToCurrentPriceResponse() {
    Price price = Price.builder()
        .value(new BigDecimal("99.99"))
        .currency("EUR")
        .build();

    CurrentPriceResponse response = ProductRestMapper.toCurrentPriceResponse(price);

    assertThat(response.value()).isEqualByComparingTo("99.99");
    assertThat(response.currency()).isEqualTo("EUR");
  }

  @Test
  void shouldMapProductPriceHistoryToResponse() {
    ProductPriceHistory productPriceHistory = ProductPriceHistory.builder()
        .name("camiseta")
        .description("camiseta One love")
        .prices(List.of(
            Price.builder()
                .id(1L)
                .value(new BigDecimal("99.99"))
                .currency("EUR")
                .initDate(LocalDate.of(2024, 1, 1))
                .endDate(null)
                .build()
        ))
        .page(0)
        .size(10)
        .totalElements(1)
        .totalPages(1)
        .build();

    ProductPriceHistoryResponse response = ProductRestMapper.toProductPriceHistoryResponse(productPriceHistory);

    assertThat(response.name()).isEqualTo("camiseta");
    assertThat(response.description()).isEqualTo("camiseta One love");
    assertThat(response.prices()).hasSize(1);
    assertThat(response.prices().getFirst().id()).isEqualTo(1L);
    assertThat(response.prices().getFirst().value()).isEqualByComparingTo("99.99");
    assertThat(response.prices().getFirst().currency()).isEqualTo("EUR");
    assertThat(response.page()).isZero();
    assertThat(response.size()).isEqualTo(10);
    assertThat(response.totalElements()).isEqualTo(1);
    assertThat(response.totalPages()).isEqualTo(1);
  }
}
