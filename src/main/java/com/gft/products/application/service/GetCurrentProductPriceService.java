package com.gft.products.application.service;

import com.gft.products.application.port.in.GetCurrentProductPricePort;
import com.gft.products.domain.model.Price;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GetCurrentProductPriceService implements GetCurrentProductPricePort {

  @Override
  public Price getCurrentProductPrice(Long productId, LocalDate date) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
