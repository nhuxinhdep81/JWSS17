package com.tien.repository;

import com.tien.model.Customer;

import java.util.List;

public interface CustomerRepository {
    void save(Customer customer);
    void update(Customer customer);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, int customerId);
    Customer findByUsername(String username);
    Customer findById(int id);
    List<Customer> findAllPaginated(int page, int size);
    long countAll();
    long countByStatus(boolean status);
    List<Customer> findByUsernameContainingPaginated(String username, int page, int size);
    long countByUsernameContaining(String username);
}