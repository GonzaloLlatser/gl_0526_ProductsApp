package com.gft.products.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePriceRequest(
    @NotNull
    @Positive
    BigDecimal value,

    @NotNull
    LocalDate initDate,

    LocalDate endDate
) {
}
