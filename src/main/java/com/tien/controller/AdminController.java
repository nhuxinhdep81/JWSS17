package com.tien.controller;

import com.tien.model.Customer;
import com.tien.service.CustomerService;
import com.tien.service.OrderService;
import com.tien.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
            return "redirect:/login";
        }
        // Thống kê tổng số người dùng người dùng
        model.addAttribute("totalCustomers", customerService.countAllCustomers());

        // Thống kê tổng sản phẩm sản phẩm
        model.addAttribute("totalProducts", productService.countAllProducts());

        // Thống kê tổng đơn hàng
        model.addAttribute("totalOrders", orderService.countAllOrders());

        // Thống kê doanh thu
        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        return "admin";
    }
}
