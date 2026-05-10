package com.gft.products.infrastructure.adapter.in.rest.mapper;

import com.gft.products.domain.model.Price;
import com.gft.products.domain.model.Product;
import com.gft.products.domain.model.ProductPriceHistory;
import com.gft.products.infrastructure.adapter.in.rest.dto.CurrentPriceResponse;
import com.gft.products.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.gft.products.infrastructure.adapter.in.rest.dto.ProductPriceHistoryResponse;
import com.gft.products.infrastructure.adapter.in.rest.dto.ProductResponse;

import java.util.List;

public final class ProductRestMapper {

  private ProductRestMapper() {
  }

  public static ProductResponse toProductResponse(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getName(),
        product.getDescription()
    );
  }

  public static PriceResponse toPriceResponse(Price price) {
    return new PriceResponse(
        price.getValue(),
        price.getInitDate(),
        price.getEndDate()
    );
  }

  public static CurrentPriceResponse toCurrentPriceResponse(Price price) {
    return new CurrentPriceResponse(price.getValue());
  }

  public static ProductPriceHistoryResponse toProductPriceHistoryResponse(ProductPriceHistory productPriceHistory) {
    List<PriceResponse> prices = productPriceHistory.getPrices().stream()
        .map(ProductRestMapper::toPriceResponse)
        .toList();

    return new ProductPriceHistoryResponse(
        productPriceHistory.getName(),
        productPriceHistory.getDescription(),
        prices,
        productPriceHistory.getPage(),
        productPriceHistory.getSize(),
        productPriceHistory.getTotalElements(),
        productPriceHistory.getTotalPages()
    );
  }
}
