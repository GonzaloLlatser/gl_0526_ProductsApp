package com.gft.products.application.service;

import com.gft.products.application.exceptions.InvalidPriceDateRangeException;
import com.gft.products.application.exceptions.PriceDateOverlapException;
import com.gft.products.application.exceptions.PriceNotFoundException;
import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProductPriceServiceTest {

  private static final Long PRODUCT_ID = 1L;
  private static final Long PRICE_ID = 2L;

  @Mock
  private ProductRepositoryPort productRepositoryPort;

  @Mock
  private PriceRepositoryPort priceRepositoryPort;

  @InjectMocks
  private UpdateProductPriceService updateProductPriceService;

  @Test
  void shouldUpdateProductPriceWhenRequestIsValid() {
    Price existingPrice = Price.builder()
        .id(PRICE_ID)
        .value(new BigDecimal("99.99"))
        .currency("EUR")
        .initDate(LocalDate.of(2024, 1, 1))
        .endDate(LocalDate.of(2024, 6, 30))
        .build();
    Price savedPrice = Price.builder()
        .id(PRICE_ID)
        .value(new BigDecimal("109.99"))
        .currency("USD")
        .initDate(LocalDate.of(2024, 1, 1))
        .endDate(LocalDate.of(2024, 6, 30))
        .build();

    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.findByIdAndProductId(PRODUCT_ID, PRICE_ID)).thenReturn(Optional.of(existingPrice));
    when(priceRepositoryPort.existsOverlappingPriceExcludingId(eq(PRODUCT_ID), any(Price.class))).thenReturn(false);
    when(priceRepositoryPort.save(eq(PRODUCT_ID), any(Price.class))).thenReturn(savedPrice);

    Price result = updateProductPriceService.updateProductPrice(
        PRODUCT_ID,
        PRICE_ID,
        new BigDecimal("109.99"),
        "usd",
        LocalDate.of(2024, 1, 1),
        LocalDate.of(2024, 6, 30)
    );

    ArgumentCaptor<Price> priceCaptor = ArgumentCaptor.forClass(Price.class);
    verify(priceRepositoryPort).save(eq(PRODUCT_ID), priceCaptor.capture());

    assertThat(priceCaptor.getValue().getId()).isEqualTo(PRICE_ID);
    assertThat(priceCaptor.getValue().getValue()).isEqualByComparingTo("109.99");
    assertThat(priceCaptor.getValue().getCurrency()).isEqualTo("USD");
    assertThat(result).isEqualTo(savedPrice);
  }

  @Test
  void shouldRejectUpdateWhenProductDoesNotExist() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(false);

    assertThatThrownBy(() -> updateProductPriceService.updateProductPrice(
        PRODUCT_ID,
        PRICE_ID,
        BigDecimal.ONE,
        "EUR",
        LocalDate.of(2024, 1, 1),
        null
    )).isInstanceOf(ProductNotFoundException.class);
  }

  @Test
  void shouldRejectUpdateWhenPriceDoesNotBelongToProduct() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.findByIdAndProductId(PRODUCT_ID, PRICE_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> updateProductPriceService.updateProductPrice(
        PRODUCT_ID,
        PRICE_ID,
        BigDecimal.ONE,
        "EUR",
        LocalDate.of(2024, 1, 1),
        null
    )).isInstanceOf(PriceNotFoundException.class);
  }

  @Test
  void shouldRejectUpdateWhenDateRangeIsInvalid() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.findByIdAndProductId(PRODUCT_ID, PRICE_ID)).thenReturn(Optional.of(Price.builder().id(PRICE_ID).build()));

    assertThatThrownBy(() -> updateProductPriceService.updateProductPrice(
        PRODUCT_ID,
        PRICE_ID,
        BigDecimal.ONE,
        "EUR",
        LocalDate.of(2024, 1, 1),
        LocalDate.of(2024, 1, 1)
    )).isInstanceOf(InvalidPriceDateRangeException.class);
  }

  @Test
  void shouldRejectUpdateWhenDatesOverlapAnotherPrice() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.findByIdAndProductId(PRODUCT_ID, PRICE_ID)).thenReturn(Optional.of(Price.builder().id(PRICE_ID).build()));
    when(priceRepositoryPort.existsOverlappingPriceExcludingId(eq(PRODUCT_ID), any(Price.class))).thenReturn(true);

    assertThatThrownBy(() -> updateProductPriceService.updateProductPrice(
        PRODUCT_ID,
        PRICE_ID,
        BigDecimal.ONE,
        "EUR",
        LocalDate.of(2024, 1, 1),
        null
    )).isInstanceOf(PriceDateOverlapException.class);
  }
}
