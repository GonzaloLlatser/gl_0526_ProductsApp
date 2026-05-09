package com.gft.products.application.exceptions;

import java.time.LocalDate;

public class PriceNotFoundException extends RuntimeException {

  public PriceNotFoundException(Long productId, LocalDate date) {
    super("Product " + productId + " does not have an active price for date " + date);
  }
}
