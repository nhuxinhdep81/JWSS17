package com.tien.repository;

import com.tien.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAllPaginated(int page, int size);
    long countAll();
    Product findById(int id);
    void update(Product product);
    void save(Product product);
    void delete(Product product);
    long countByStockGreaterThan(int stock);
    long countByStock(int stock);
    List<Product> findByPriceRangePaginated(double minPrice, double maxPrice, int page, int size);
    long countByPriceRange(double minPrice, double maxPrice);
}