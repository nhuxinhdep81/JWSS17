package com.tien.service;

import com.tien.model.Customer;
import com.tien.model.Order;
import com.tien.model.ProductCart;

import java.util.List;

public interface OrderService {
    Order createOrder(Order orderDetails, Customer customer, List<ProductCart> cartItems) throws IllegalArgumentException;
    List<Order> getOrdersByCustomerId(int customerId, int page, int size);
    long countOrdersByCustomerId(int customerId); // Đổi tên cho rõ ràng
    void cancelOrder(int orderId, int customerId) throws IllegalArgumentException;
    Order getOrderByIdAndCustomerId(int orderId, int customerId);
}