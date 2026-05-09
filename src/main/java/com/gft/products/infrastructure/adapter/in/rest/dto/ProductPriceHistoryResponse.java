package com.gft.products.infrastructure.adapter.in.rest.dto;

import java.util.List;

public record ProductPriceHistoryResponse(
    String name,
    String description,
    List<PriceResponse> prices
) {
}
