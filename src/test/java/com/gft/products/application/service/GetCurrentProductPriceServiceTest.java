package com.gft.products.application.service;

import com.gft.products.application.exceptions.PriceNotFoundException;
import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCurrentProductPriceServiceTest {

  private static final Long PRODUCT_ID = 1L;
  private static final LocalDate DATE = LocalDate.of(2024, 4, 15);

  @Mock
  private ProductRepositoryPort productRepositoryPort;

  @Mock
  private PriceRepositoryPort priceRepositoryPort;

  @InjectMocks
  private GetCurrentProductPriceService getCurrentProductPriceService;

  @Test
  void shouldReturnCurrentPriceWhenExists() {
    Price price = Price.builder()
        .value(new BigDecimal("99.99"))
        .initDate(LocalDate.of(2024, 1, 1))
        .endDate(null)
        .build();

    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.findActiveByProductIdAndDate(PRODUCT_ID, DATE)).thenReturn(Optional.of(price));

    Price result = getCurrentProductPriceService.getCurrentProductPrice(PRODUCT_ID, DATE);

    assertThat(result).isEqualTo(price);
  }

  @Test
  void shouldRejectCurrentPriceLookupWhenProductDoesNotExist() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(false);

    assertThatThrownBy(() -> getCurrentProductPriceService.getCurrentProductPrice(PRODUCT_ID, DATE))
        .isInstanceOf(ProductNotFoundException.class);
  }

  @Test
  void shouldRejectCurrentPriceLookupWhenNoPriceExistsForDate() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.findActiveByProductIdAndDate(PRODUCT_ID, DATE)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> getCurrentProductPriceService.getCurrentProductPrice(PRODUCT_ID, DATE))
        .isInstanceOf(PriceNotFoundException.class);
  }
}
