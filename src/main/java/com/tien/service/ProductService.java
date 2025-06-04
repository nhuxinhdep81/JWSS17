package com.tien.service;

import com.tien.model.Product;
import java.util.List;

public interface ProductService {
    List<Product> getProductsPaginated(int page, int size);
    long countTotalProducts();
    Product getProductById(int id);
    void saveProduct(Product product);
    void deleteProduct(int id);
    long countAllProducts();
    long countInStockProducts();
    long countOutOfStockProducts();
    List<Product> getProductsByPriceRangePaginated(double minPrice, double maxPrice, int page, int size);
    long countProductsByPriceRange(double minPrice, double maxPrice);
}