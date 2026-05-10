package com.gft.products.infrastructure.adapter.in.rest.controller;

import com.gft.products.application.exceptions.InvalidPriceCurrencyException;
import com.gft.products.application.exceptions.InvalidPriceDateRangeException;
import com.gft.products.application.exceptions.InvalidPriceValueException;
import com.gft.products.application.exceptions.InvalidProductException;
import com.gft.products.application.exceptions.PriceDateOverlapException;
import com.gft.products.application.exceptions.PriceNotFoundException;
import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.infrastructure.adapter.in.rest.dto.ErrorDetailResponse;
import com.gft.products.infrastructure.adapter.in.rest.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException exception) {
    return error(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", exception.getMessage());
  }

  @ExceptionHandler(PriceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handlePriceNotFound(PriceNotFoundException exception) {
    return error(HttpStatus.NOT_FOUND, "PRICE_NOT_FOUND", exception.getMessage());
  }

  @ExceptionHandler(InvalidProductException.class)
  public ResponseEntity<ErrorResponse> handleInvalidProduct(InvalidProductException exception) {
    return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", exception.getMessage());
  }

  @ExceptionHandler(InvalidPriceValueException.class)
  public ResponseEntity<ErrorResponse> handleInvalidPriceValue(InvalidPriceValueException exception) {
    return error(HttpStatus.BAD_REQUEST, "INVALID_PRICE_VALUE", exception.getMessage());
  }

  @ExceptionHandler(InvalidPriceCurrencyException.class)
  public ResponseEntity<ErrorResponse> handleInvalidPriceCurrency(InvalidPriceCurrencyException exception) {
    return error(HttpStatus.BAD_REQUEST, "INVALID_PRICE_CURRENCY", exception.getMessage());
  }

  @ExceptionHandler(InvalidPriceDateRangeException.class)
  public ResponseEntity<ErrorResponse> handleInvalidPriceDateRange(InvalidPriceDateRangeException exception) {
    return error(HttpStatus.BAD_REQUEST, "INVALID_PRICE_DATE_RANGE", exception.getMessage());
  }

  @ExceptionHandler(PriceDateOverlapException.class)
  public ResponseEntity<ErrorResponse> handlePriceDateOverlap(PriceDateOverlapException exception) {
    return error(HttpStatus.CONFLICT, "PRICE_DATE_OVERLAP", exception.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    List<ErrorDetailResponse> details = exception.getBindingResult().getFieldErrors().stream()
        .map(error -> new ErrorDetailResponse(error.getField(), error.getDefaultMessage()))
        .toList();

    return ResponseEntity.badRequest()
        .body(new ErrorResponse("VALIDATION_ERROR", "Request validation failed", details));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
    List<ErrorDetailResponse> details = exception.getConstraintViolations().stream()
        .map(violation -> new ErrorDetailResponse(violation.getPropertyPath().toString(), violation.getMessage()))
        .toList();

    return ResponseEntity.badRequest()
        .body(new ErrorResponse("VALIDATION_ERROR", "Request validation failed", details));
  }

  @ExceptionHandler({
      HttpMessageNotReadableException.class,
      MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<ErrorResponse> handleInvalidRequest(Exception exception) {
    return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Request body or parameters are invalid");
  }

  private ResponseEntity<ErrorResponse> error(HttpStatus status, String code, String message) {
    return ResponseEntity.status(status)
        .body(new ErrorResponse(code, message, List.of()));
  }
}
