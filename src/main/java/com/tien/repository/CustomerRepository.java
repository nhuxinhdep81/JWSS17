package com.tien.repository;

import com.tien.model.Customer;

public interface CustomerRepository {
    void save(Customer customer);
    void update(Customer customer);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, int customerId);
    Customer findByUsername(String username);
    Customer findById(int id);
}