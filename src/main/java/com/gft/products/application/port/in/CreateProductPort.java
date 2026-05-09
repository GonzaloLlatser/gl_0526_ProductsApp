package com.gft.products.application.port.in;

import com.gft.products.domain.model.Product;

public interface CreateProductPort {

  Product createProduct(String name, String description);
}
