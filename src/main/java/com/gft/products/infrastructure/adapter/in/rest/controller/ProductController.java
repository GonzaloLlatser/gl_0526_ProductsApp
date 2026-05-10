package com.gft.products.infrastructure.adapter.in.rest.controller;

import com.gft.products.application.port.in.AddProductPricePort;
import com.gft.products.application.port.in.CreateProductPort;
import com.gft.products.application.port.in.GetCurrentProductPricePort;
import com.gft.products.application.port.in.GetProductPriceHistoryPort;
import com.gft.products.application.port.in.UpdateProductPricePort;
import com.gft.products.domain.model.Price;
import com.gft.products.domain.model.Product;
import com.gft.products.domain.model.ProductPriceHistory;
import com.gft.products.infrastructure.adapter.in.rest.dto.CreatePriceRequest;
import com.gft.products.infrastructure.adapter.in.rest.dto.CreateProductRequest;
import com.gft.products.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.gft.products.infrastructure.adapter.in.rest.dto.ProductResponse;
import com.gft.products.infrastructure.adapter.in.rest.mapper.ProductRestMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

  private final CreateProductPort createProductPort;
  private final AddProductPricePort addProductPricePort;
  private final UpdateProductPricePort updateProductPricePort;
  private final GetCurrentProductPricePort getCurrentProductPricePort;
  private final GetProductPriceHistoryPort getProductPriceHistoryPort;

  @PostMapping
  public ResponseEntity<ProductResponse> createProduct(
      @Valid @RequestBody CreateProductRequest request) {
    Product product = createProductPort.createProduct(request.name(), request.description());
    return ResponseEntity.status(201)
        .body(ProductRestMapper.toProductResponse(product));
  }

  @PostMapping("/{id}/prices")
  public ResponseEntity<PriceResponse> addProductPrice(
      @Positive @PathVariable Long id,
      @Valid @RequestBody CreatePriceRequest request) {
    Price price = addProductPricePort.addProductPrice(
        id,
        request.value(),
        request.currency(),
        request.initDate(),
        request.endDate());
    return ResponseEntity.status(201)
        .body(ProductRestMapper.toPriceResponse(price));
  }

  @PutMapping("/{productId}/prices/{priceId}")
  public ResponseEntity<PriceResponse> updateProductPrice(
      @Positive @PathVariable Long productId,
      @Positive @PathVariable Long priceId,
      @Valid @RequestBody CreatePriceRequest request) {
    Price price = updateProductPricePort.updateProductPrice(
        productId,
        priceId,
        request.value(),
        request.currency(),
        request.initDate(),
        request.endDate());
    return ResponseEntity.ok(ProductRestMapper.toPriceResponse(price));
  }

  @GetMapping("/{id}/prices")
  public ResponseEntity<?> getProductPrices(
      @Positive @PathVariable Long id,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
      @Min(0) @RequestParam(defaultValue = "0") int page,
      @Min(1) @Max(100) @RequestParam(defaultValue = "10") int size,
      @Pattern(regexp = "^(initDate|endDate|value),(asc|desc)$")
      @RequestParam(defaultValue = "initDate,asc") String sort) {
    if (date != null) {
      Price price = getCurrentProductPricePort.getCurrentProductPrice(id, date);
      return ResponseEntity.ok(ProductRestMapper.toCurrentPriceResponse(price));
    }
    ProductPriceHistory productPriceHistory = getProductPriceHistoryPort.getProductPriceHistory(id, page, size, sort);
    return ResponseEntity.ok(ProductRestMapper.toProductPriceHistoryResponse(productPriceHistory));
  }
}
