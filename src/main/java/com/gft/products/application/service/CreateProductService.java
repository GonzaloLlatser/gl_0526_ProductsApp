package com.gft.products.application.service;

import com.gft.products.application.port.in.CreateProductPort;
import com.gft.products.domain.model.Product;
import org.springframework.stereotype.Service;

@Service
public class CreateProductService implements CreateProductPort {

  @Override
  public Product createProduct(String name, String description) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
