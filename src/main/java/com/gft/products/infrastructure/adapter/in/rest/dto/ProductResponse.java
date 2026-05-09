package com.gft.products.infrastructure.adapter.in.rest.dto;

public record ProductResponse(
    Long id,
    String name,
    String description
) {
}
