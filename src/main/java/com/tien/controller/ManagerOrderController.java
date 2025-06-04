package com.tien.controller;

import com.tien.model.Order;
import com.tien.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class ManagerOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "3") int pageSize,
            @RequestParam(name = "recipientName", required = false) String recipientName,
            @RequestParam(name = "status", required = false) String status,
            Model model) {
        List<Order> orders;
        long totalOrders;
        long totalPages;

        // Nếu có recipientName hoặc status, tìm kiếm theo tiêu chí
        if ((recipientName != null && !recipientName.trim().isEmpty()) || (status != null && !status.trim().isEmpty())) {
            orders = orderService.searchOrdersByRecipientNameAndStatus(recipientName, status, page, pageSize);
            totalOrders = orderService.countOrdersByRecipientNameAndStatus(recipientName, status);
        } else {
            orders = orderService.getAllOrders(page, pageSize);
            totalOrders = orderService.countAllOrders();
        }

        totalPages = (totalOrders + pageSize - 1) / pageSize;

        System.out.println("Total Orders: " + totalOrders + ", Orders Size: " + orders.size());
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("recipientName", recipientName);
        model.addAttribute("status", status);
        return "manager_orders";
    }

    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam("orderId") int orderId,
                                    @RequestParam("status") String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders?statusUpdated=true";
    }
}