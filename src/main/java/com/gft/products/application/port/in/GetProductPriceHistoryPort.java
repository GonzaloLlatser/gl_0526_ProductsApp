package com.gft.products.application.port.in;

import com.gft.products.domain.model.ProductPriceHistory;

public interface GetProductPriceHistoryPort {

  ProductPriceHistory getProductPriceHistory(Long productId, int page, int size, String sort);
}
