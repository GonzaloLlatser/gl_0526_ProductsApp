package com.gft.products.infrastructure.adapter.in.rest.dto;

import java.util.List;

public record ErrorResponse(
    String code,
    String message,
    List<ErrorDetailResponse> details
) {
}
