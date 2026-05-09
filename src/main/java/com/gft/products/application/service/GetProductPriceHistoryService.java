package com.gft.products.application.service;

import com.gft.products.application.port.in.GetProductPriceHistoryPort;
import com.gft.products.domain.model.Product;
import org.springframework.stereotype.Service;

@Service
public class GetProductPriceHistoryService implements GetProductPriceHistoryPort {

  @Override
  public Product getProductPriceHistory(Long productId) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
