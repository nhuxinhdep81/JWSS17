package com.tien.repository;

import com.tien.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAllPaginated(int page, int size);
    long countAll();
    Product findById(int id);
    void update(Product product);
}