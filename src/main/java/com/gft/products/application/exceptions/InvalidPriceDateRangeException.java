package com.gft.products.application.exceptions;

public class InvalidPriceDateRangeException extends RuntimeException {

  public InvalidPriceDateRangeException() {
    super("initDate must be before endDate");
  }
}
