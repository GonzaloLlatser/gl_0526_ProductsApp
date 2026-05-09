package com.gft.products.application.exceptions;

public class InvalidProductException extends RuntimeException {

  public InvalidProductException() {
    super("Product name must not be blank");
  }
}
