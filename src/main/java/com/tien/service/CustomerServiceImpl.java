package com.tien.service;

import com.tien.model.Customer;
import com.tien.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer register(Customer customer) {
        customer.setRole("USER");
        customer.setStatus(true);
        customerRepository.save(customer);
        return customer;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return customerRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean isEmailTakenByOtherUser(String email, int currentUserId) {
        return customerRepository.existsByEmailAndIdNot(email, currentUserId);
    }


    @Override
    public Customer login(String username, String password) {
        Customer customer = customerRepository.findByUsername(username);
        if (customer != null) {
            if (customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.update(customer);
    }

    @Override
    public Customer findById(int id) {
        return customerRepository.findById(id);
    }
}