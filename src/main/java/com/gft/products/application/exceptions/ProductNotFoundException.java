package com.gft.products.application.exceptions;

public class ProductNotFoundException extends RuntimeException {

  public ProductNotFoundException(Long productId) {
    super("Product was not found: " + productId);
  }
}
