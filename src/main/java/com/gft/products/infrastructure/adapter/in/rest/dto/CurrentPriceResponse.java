package com.gft.products.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;

public record CurrentPriceResponse(
    BigDecimal value,
    String currency
) {
}
