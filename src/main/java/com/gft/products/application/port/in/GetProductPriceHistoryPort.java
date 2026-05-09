package com.gft.products.application.port.in;

import com.gft.products.domain.model.Product;

public interface GetProductPriceHistoryPort {

  Product getProductPriceHistory(Long productId);
}
