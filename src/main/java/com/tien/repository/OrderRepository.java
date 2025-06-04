package com.tien.repository;

import com.tien.model.Order;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);
    void update(Order order);
    Order findByIdAndCustomerId(int orderId, int customerId);
    List<Order> findByCustomerId(int customerId, int page, int size);
    long countByCustomerId(int customerId);
    Order findById(int orderId);
    List<Order> findAllPaginated(int page, int size);
    long countAll();
    long countByStatus(String status);
    double getTotalRevenue();
    List<Order> findByRecipientNameAndStatusPaginated(String recipientName, String status, int page, int size);
    long countByRecipientNameAndStatus(String recipientName, String status);
}