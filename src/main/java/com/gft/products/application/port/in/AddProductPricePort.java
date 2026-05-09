package com.gft.products.application.port.in;

import com.gft.products.domain.model.Price;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AddProductPricePort {

  Price addProductPrice(Long productId, BigDecimal value, LocalDate initDate, LocalDate endDate);
}
