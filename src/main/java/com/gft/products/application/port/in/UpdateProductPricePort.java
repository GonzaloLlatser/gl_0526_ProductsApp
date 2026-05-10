package com.gft.products.application.port.in;

import com.gft.products.domain.model.Price;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UpdateProductPricePort {

  Price updateProductPrice(Long productId, Long priceId, BigDecimal value, String currency, LocalDate initDate, LocalDate endDate);
}
