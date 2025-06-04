package com.tien.service;

import com.tien.model.Customer;
import com.tien.model.Order;
import com.tien.model.ProductCart;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order createOrder(Order orderDetails, Customer customer, List<ProductCart> cartItems) throws IllegalArgumentException;
    List<Order> getOrdersByCustomerId(int customerId, int page, int size);
    long countOrdersByCustomerId(int customerId);
    void cancelOrder(int orderId, int customerId) throws IllegalArgumentException;
    Order getOrderByIdAndCustomerId(int orderId, int customerId);
    List<Order> getAllOrders(int page, int size);
    long countAllOrders();
    void updateOrderStatus(int orderId, String status);
    Map<String, Long> countOrdersByStatus();
    double getTotalRevenue();
    List<Order> searchOrdersByRecipientNameAndStatus(String recipientName, String status, int page, int size);
    long countOrdersByRecipientNameAndStatus(String recipientName, String status);
}