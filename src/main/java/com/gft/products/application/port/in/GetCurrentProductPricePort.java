package com.gft.products.application.port.in;

import com.gft.products.domain.model.Price;

import java.time.LocalDate;

public interface GetCurrentProductPricePort {

  Price getCurrentProductPrice(Long productId, LocalDate date);
}
