package com.tien.controller;

import com.tien.model.Customer;
import com.tien.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class ManagerUserController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listUsers(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "3") int pageSize,
            @RequestParam(name = "username", required = false) String username,
            Model model) {
        List<Customer> customers;
        long totalCustomers;
        long totalPages;

        // Nếu có username, tìm kiếm theo username
        if (username != null && !username.trim().isEmpty()) {
            customers = customerService.searchCustomersByUsername(username, page, pageSize);
            totalCustomers = customerService.countCustomersByUsername(username);
        } else {
            customers = customerService.getAllCustomers(page, pageSize);
            totalCustomers = customerService.countAllCustomers();
        }

        totalPages = (totalCustomers + pageSize - 1) / pageSize;

        System.out.println("Total Customers: " + totalCustomers + ", Customers Size: " + customers.size());
        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("username", username);
        return "manager_user";
    }

    @GetMapping("/toggle-status")
    public String toggleUserStatus(@RequestParam("customerId") int customerId) {
        customerService.toggleCustomerStatus(customerId);
        return "redirect:/admin/users?statusToggled=true";
    }
}