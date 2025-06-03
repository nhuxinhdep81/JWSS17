package com.tien.repository;

import com.tien.model.ProductCart;

import java.util.List;

public interface ProductCartRepository {
    ProductCart saveOrUpdate(ProductCart productCart);
    ProductCart findByCustomerIdAndProductId(int customerId, int productId);
    List<ProductCart> findByCustomerId(int customerId);
    void delete(ProductCart productCart);
    ProductCart findByIdAndCustomerId(int cartId, int customerId);
    void deleteAllByCustomerId(int customerId);
}