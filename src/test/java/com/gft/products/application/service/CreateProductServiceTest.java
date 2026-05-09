package com.gft.products.application.service;

import com.gft.products.application.exceptions.InvalidProductException;
import com.gft.products.application.port.out.ProductRepositoryPort;
import com.gft.products.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

  @Mock
  private ProductRepositoryPort productRepositoryPort;

  @InjectMocks
  private CreateProductService createProductService;

  @Test
  void shouldCreateProductWhenNameIsValid() {
    Product savedProduct = Product.builder()
        .id(1L)
        .name("camiseta hombre One love")
        .description("camiseta hombre One love")
        .build();

    when(productRepositoryPort.save(org.mockito.ArgumentMatchers.any(Product.class))).thenReturn(savedProduct);

    Product result = createProductService.createProduct(" camiseta hombre One love ", "camiseta hombre One love");

    ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
    verify(productRepositoryPort).save(productCaptor.capture());

    assertThat(productCaptor.getValue().getName()).isEqualTo("camiseta hombre One love");
    assertThat(productCaptor.getValue().getDescription()).isEqualTo("camiseta hombre One love");
    assertThat(result).isEqualTo(savedProduct);
  }

  @Test
  void shouldRejectBlankProductName() {
    assertThatThrownBy(() -> createProductService.createProduct(" ", "Description"))
        .isInstanceOf(InvalidProductException.class);
  }

  @Test
  void shouldRejectNullProductName() {
    assertThatThrownBy(() -> createProductService.createProduct(null, "Description"))
        .isInstanceOf(InvalidProductException.class);
  }
}
