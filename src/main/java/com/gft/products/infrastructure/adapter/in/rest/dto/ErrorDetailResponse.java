package com.gft.products.infrastructure.adapter.in.rest.dto;

public record ErrorDetailResponse(
    String field,
    String message
) {
}
