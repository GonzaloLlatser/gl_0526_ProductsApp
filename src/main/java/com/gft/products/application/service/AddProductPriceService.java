package com.gft.products.application.service;

import com.gft.products.application.port.in.AddProductPricePort;
import com.gft.products.domain.model.Price;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AddProductPriceService implements AddProductPricePort {

  @Override
  public Price addProductPrice(Long productId, BigDecimal value, LocalDate initDate, LocalDate endDate) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
