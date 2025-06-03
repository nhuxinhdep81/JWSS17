package com.tien.service;

import com.tien.model.Product;
import com.tien.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // Mặc định readOnly cho các phương thức get
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
}