package com.gft.products.application.exceptions;

public class PriceDateOverlapException extends RuntimeException {

  public PriceDateOverlapException() {
    super("Price range overlaps with an existing product price");
  }
}
