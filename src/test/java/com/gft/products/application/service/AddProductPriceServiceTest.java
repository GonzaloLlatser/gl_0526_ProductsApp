package com.gft.products.application.service;

import com.gft.products.application.exceptions.InvalidPriceDateRangeException;
import com.gft.products.application.exceptions.InvalidPriceCurrencyException;
import com.gft.products.application.exceptions.InvalidPriceValueException;
import com.gft.products.application.exceptions.PriceDateOverlapException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductPriceServiceTest {

  private static final Long PRODUCT_ID = 1L;

  @Mock
  private ProductRepositoryPort productRepositoryPort;

  @Mock
  private PriceRepositoryPort priceRepositoryPort;

  @InjectMocks
  private AddProductPriceService addProductPriceService;

  @Test
  void shouldAddProductPriceWhenRequestIsValid() {
    Price savedPrice = Price.builder()
        .id(1L)
        .value(new BigDecimal("99.99"))
        .currency("EUR")
        .initDate(LocalDate.of(2024, 1, 1))
        .endDate(LocalDate.of(2024, 12, 31))
        .build();

    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.existsOverlappingPrice(org.mockito.ArgumentMatchers.eq(PRODUCT_ID), any(Price.class)))
        .thenReturn(false);
    when(priceRepositoryPort.save(org.mockito.ArgumentMatchers.eq(PRODUCT_ID), any(Price.class)))
        .thenReturn(savedPrice);

    Price result = addProductPriceService.addProductPrice(
        PRODUCT_ID,
        new BigDecimal("99.99"),
        "eur",
        LocalDate.of(2024, 1, 1),
        LocalDate.of(2024, 12, 31)
    );

    ArgumentCaptor<Price> priceCaptor = ArgumentCaptor.forClass(Price.class);
    verify(priceRepositoryPort).save(org.mockito.ArgumentMatchers.eq(PRODUCT_ID), priceCaptor.capture());

    assertThat(priceCaptor.getValue().getValue()).isEqualByComparingTo("99.99");
    assertThat(priceCaptor.getValue().getCurrency()).isEqualTo("EUR");
    assertThat(priceCaptor.getValue().getInitDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    assertThat(priceCaptor.getValue().getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31));
    assertThat(result).isEqualTo(savedPrice);
  }

  @Test
  void shouldRejectPriceWhenProductDoesNotExist() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(false);

    assertThatThrownBy(() -> addProductPriceService.addProductPrice(
        PRODUCT_ID,
        BigDecimal.ONE,
        "EUR",
        LocalDate.of(2024, 1, 1),
        null
    )).isInstanceOf(ProductNotFoundException.class);

    verify(priceRepositoryPort, never()).save(org.mockito.ArgumentMatchers.anyLong(), any(Price.class));
  }

  @Test
  void shouldRejectPriceWhenValueIsZeroOrNegative() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);

    assertThatThrownBy(() -> addProductPriceService.addProductPrice(
        PRODUCT_ID,
        BigDecimal.ZERO,
        "EUR",
        LocalDate.of(2024, 1, 1),
        null
    )).isInstanceOf(InvalidPriceValueException.class);
  }

  @Test
  void shouldRejectPriceWhenCurrencyIsInvalid() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);

    assertThatThrownBy(() -> addProductPriceService.addProductPrice(
        PRODUCT_ID,
        BigDecimal.ONE,
        "EU",
        LocalDate.of(2024, 1, 1),
        null
    )).isInstanceOf(InvalidPriceCurrencyException.class);
  }

  @Test
  void shouldRejectPriceWhenInitDateIsNotBeforeEndDate() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);

    assertThatThrownBy(() -> addProductPriceService.addProductPrice(
        PRODUCT_ID,
        BigDecimal.ONE,
        "EUR",
        LocalDate.of(2024, 1, 1),
        LocalDate.of(2024, 1, 1)
    )).isInstanceOf(InvalidPriceDateRangeException.class);
  }

  @Test
  void shouldRejectPriceWhenDatesOverlap() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.existsOverlappingPrice(org.mockito.ArgumentMatchers.eq(PRODUCT_ID), any(Price.class)))
        .thenReturn(true);

    assertThatThrownBy(() -> addProductPriceService.addProductPrice(
        PRODUCT_ID,
        BigDecimal.ONE,
        "EUR",
        LocalDate.of(2024, 1, 1),
        null
    )).isInstanceOf(PriceDateOverlapException.class);
  }
}
