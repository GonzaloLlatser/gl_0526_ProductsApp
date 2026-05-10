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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteProductPriceServiceTest {

  private static final Long PRODUCT_ID = 1L;
  private static final Long PRICE_ID = 2L;

  @Mock
  private ProductRepositoryPort productRepositoryPort;

  @Mock
  private PriceRepositoryPort priceRepositoryPort;

  @InjectMocks
  private DeleteProductPriceService deleteProductPriceService;

  @Test
  void shouldDeleteProductPriceWhenItBelongsToProduct() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.findByIdAndProductId(PRODUCT_ID, PRICE_ID))
        .thenReturn(Optional.of(Price.builder().id(PRICE_ID).build()));

    deleteProductPriceService.deleteProductPrice(PRODUCT_ID, PRICE_ID);

    verify(priceRepositoryPort).deleteById(PRICE_ID);
  }

  @Test
  void shouldRejectDeleteWhenProductDoesNotExist() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(false);

    assertThatThrownBy(() -> deleteProductPriceService.deleteProductPrice(PRODUCT_ID, PRICE_ID))
        .isInstanceOf(ProductNotFoundException.class);

    verify(priceRepositoryPort, never()).deleteById(PRICE_ID);
  }

  @Test
  void shouldRejectDeleteWhenPriceDoesNotBelongToProduct() {
    when(productRepositoryPort.existsById(PRODUCT_ID)).thenReturn(true);
    when(priceRepositoryPort.findByIdAndProductId(PRODUCT_ID, PRICE_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> deleteProductPriceService.deleteProductPrice(PRODUCT_ID, PRICE_ID))
        .isInstanceOf(PriceNotFoundException.class);

    verify(priceRepositoryPort, never()).deleteById(PRICE_ID);
  }
}
