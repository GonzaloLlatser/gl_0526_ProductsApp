package com.gft.products.application.service;

import com.gft.products.application.exceptions.PriceNotFoundException;
import com.gft.products.application.exceptions.ProductNotFoundException;
import com.gft.products.application.port.in.DeleteProductPricePort;
import com.gft.products.application.port.out.PriceRepositoryPort;
import com.gft.products.application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeleteProductPriceService implements DeleteProductPricePort {

  private final ProductRepositoryPort productRepositoryPort;
  private final PriceRepositoryPort priceRepositoryPort;

  @Override
  public void deleteProductPrice(Long productId, Long priceId) {
    Objects.requireNonNull(productId);
    Objects.requireNonNull(priceId);

    if (!productRepositoryPort.existsById(productId)) {
      throw new ProductNotFoundException(productId);
    }

    priceRepositoryPort.findByIdAndProductId(productId, priceId)
        .orElseThrow(() -> new PriceNotFoundException(productId, priceId));

    priceRepositoryPort.deleteById(priceId);
  }
}
