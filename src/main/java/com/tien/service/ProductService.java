package com.tien.service;

import com.tien.model.Product;
import java.util.List;

public interface ProductService {
    List<Product> getProductsPaginated(int page, int size);
    long countTotalProducts();
    Product getProductById(int id);
}