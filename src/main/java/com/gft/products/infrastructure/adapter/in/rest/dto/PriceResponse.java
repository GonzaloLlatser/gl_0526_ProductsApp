package com.gft.products.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceResponse(
    BigDecimal value,
    LocalDate initDate,
    LocalDate endDate
) {
}
