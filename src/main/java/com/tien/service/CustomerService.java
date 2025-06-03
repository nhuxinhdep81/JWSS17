package com.tien.service;

import com.tien.model.Customer;

public interface CustomerService {
    Customer register(Customer customer);
    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
    boolean isEmailTakenByOtherUser(String email, int currentUserId);
    Customer login(String username, String password);
    void updateCustomer(Customer customer);
    Customer findById(int id);
}