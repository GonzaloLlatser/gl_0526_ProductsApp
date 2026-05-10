package com.gft.products.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceResponse(
    Long id,
    BigDecimal value,
    String currency,
    LocalDate initDate,
    LocalDate endDate
) {
}
