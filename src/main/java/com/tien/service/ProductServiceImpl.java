package com.tien.service;

import com.tien.model.Product;
import com.tien.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getProductsPaginated(int page, int size) {
        return productRepository.findAllPaginated(page, size);
    }

    @Override
    public long countTotalProducts() {
        return productRepository.countAll();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        Product product = productRepository.findById(id);
        if (product != null) {
            productRepository.delete(product);
        }
    }

    @Override
    public long countAllProducts() {
        return productRepository.countAll();
    }

    @Override
    public long countInStockProducts() {
        return productRepository.countByStockGreaterThan(0);
    }

    @Override
    public long countOutOfStockProducts() {
        return productRepository.countByStock(0);
    }

    @Override
    public List<Product> getProductsByPriceRangePaginated(double minPrice, double maxPrice, int page, int size) {
        return productRepository.findByPriceRangePaginated(minPrice, maxPrice, page, size);
    }

    @Override
    public long countProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.countByPriceRange(minPrice, maxPrice);
    }
}