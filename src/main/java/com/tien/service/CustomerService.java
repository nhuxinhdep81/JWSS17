package com.tien.service;

import com.tien.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer register(Customer customer);
    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
    boolean isEmailTakenByOtherUser(String email, int currentUserId);
    Customer login(String username, String password);
    void updateCustomer(Customer customer);
    Customer findById(int id);
    List<Customer> getAllCustomers(int page, int size);
    long countAllCustomers();
    void toggleCustomerStatus(int customerId);
    long countActiveCustomers();
    long countInactiveCustomers();
    List<Customer> searchCustomersByUsername(String username, int page, int size);
    long countCustomersByUsername(String username);
}