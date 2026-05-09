package com.gft.products.application.exceptions;

public class InvalidPriceValueException extends RuntimeException {

  public InvalidPriceValueException() {
    super("Price value must be greater than zero");
  }
}
