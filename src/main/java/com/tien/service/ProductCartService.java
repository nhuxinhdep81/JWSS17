package com.tien.service;

import com.tien.model.ProductCart;
import java.util.List;

public interface ProductCartService {
    ProductCart addToCart(int customerId, int productId, int quantity) throws IllegalArgumentException;
    List<ProductCart> getCartItemsByCustomerId(int customerId);
    ProductCart updateCartItemQuantity(int customerId, int cartId, int quantity) throws IllegalArgumentException;
    void removeCartItem(int customerId, int cartId) throws IllegalArgumentException;
    void clearCart(int customerId);
}