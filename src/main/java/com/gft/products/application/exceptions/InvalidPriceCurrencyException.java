package com.gft.products.application.exceptions;

public class InvalidPriceCurrencyException extends RuntimeException {

  public InvalidPriceCurrencyException() {
    super("currency must use ISO 4217 format");
  }
}
