package com.tien.controller;

import com.tien.model.Customer;
import com.tien.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("customer") @Valid Customer customer,
                                  BindingResult result,
                                  Model model) {
        if (customerService.isUsernameTaken(customer.getUsername())) {
            result.rejectValue("username", "error.username", "Tên đăng nhập đã tồn tại");
        }
        if (customerService.isEmailTaken(customer.getEmail())) {
            result.rejectValue("email", "error.email", "Email đã được sử dụng");
        }

        if (result.hasErrors()) {
            return "register";
        }

        customerService.register(customer);
        model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        model.addAttribute("customer", new Customer()); // Reset form
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("customer")) {
            model.addAttribute("customer", new Customer());
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("customer") Customer customer,
                               Model model,
                               HttpSession session) {

        Customer loggedIn = customerService.login(customer.getUsername(), customer.getPassword());
        if (loggedIn == null) {
            model.addAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu");
            model.addAttribute("customer", customer);
            return "login";
        }
        if (!loggedIn.isStatus()){
            model.addAttribute("errorMessage", "Tài khoản của bạn đã bị khóa.");
            model.addAttribute("customer", customer);
            return "login";
        }


        session.setAttribute("loggedInUser", loggedIn);

        if ("ADMIN".equalsIgnoreCase(loggedIn.getRole())) {
            return "redirect:/admin";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logoutSuccess=true";
    }

    @GetMapping("/admin")
    public String adminPage(HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
            return "redirect:/login";
        }
        return "admin";
    }
}