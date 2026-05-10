package com.gft.products.application.service;

import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Price;
import com.gft.products.domain.model.PricePage;
import com.gft.products.domain.model.Product;
import com.gft.products.domain.model.ProductPriceHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProductPriceHistoryServiceTest {

  private static final Long PRODUCT_ID = 1L;

  @Mock
  private ProductRepositoryPort productRepositoryPort;

  @Mock
  private PriceRepositoryPort priceRepositoryPort;

  @InjectMocks
  private GetProductPriceHistoryService getProductPriceHistoryService;

  @Test
  void shouldReturnProductPriceHistoryWhenProductExists() {
    Product product = Product.builder()
        .id(PRODUCT_ID)
        .name("camiseta")
        .description("camiseta hombre One love")
        .build();
    List<Price> prices = List.of(
        Price.builder()
            .value(new BigDecimal("99.99"))
            .currency("EUR")
            .initDate(LocalDate.of(2024, 1, 1))
            .endDate(LocalDate.of(2024, 6, 30))
            .build()
    );

    when(productRepositoryPort.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
    when(priceRepositoryPort.findByProductId(PRODUCT_ID, 0, 10, "initDate", true))
        .thenReturn(PricePage.builder()
            .prices(prices)
            .page(0)
            .size(10)
            .totalElements(1)
            .totalPages(1)
            .build());

    ProductPriceHistory result = getProductPriceHistoryService.getProductPriceHistory(PRODUCT_ID, 0, 10, "initDate,asc");

    assertThat(result.getName()).isEqualTo("camiseta");
    assertThat(result.getDescription()).isEqualTo("camiseta hombre One love");
    assertThat(result.getPrices()).isEqualTo(prices);
    assertThat(result.getPage()).isZero();
    assertThat(result.getSize()).isEqualTo(10);
    assertThat(result.getTotalElements()).isEqualTo(1);
    assertThat(result.getTotalPages()).isEqualTo(1);
  }

  @Test
  void shouldRejectPriceHistoryLookupWhenProductDoesNotExist() {
    when(productRepositoryPort.findById(PRODUCT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> getProductPriceHistoryService.getProductPriceHistory(PRODUCT_ID, 0, 10, "initDate,asc"))
        .isInstanceOf(ProductNotFoundException.class);
  }
}
