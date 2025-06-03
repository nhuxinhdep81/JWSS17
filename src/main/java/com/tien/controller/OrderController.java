package com.tien.controller;

import com.tien.model.Customer;
import com.tien.model.Order;
import com.tien.model.ProductCart;
import com.tien.service.OrderService;
import com.tien.service.ProductCartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private ProductCartService productCartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String showCheckoutForm(HttpSession session, Model model) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<ProductCart> cartItems = productCartService.getCartItemsByCustomerId(loggedInUser.getId());
        if (cartItems.isEmpty()) {
            model.addAttribute("checkoutError", "Giỏ hàng của bạn đang trống. Không thể thanh toán.");
        }
        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();


        if (!model.containsAttribute("order")) {
            Order order = new Order();
            order.setRecipientName(loggedInUser.getUsername());
            order.setPhoneNumber(loggedInUser.getPhoneNumber());
            model.addAttribute("order", order);
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", total);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@Valid @ModelAttribute("order") Order order,
                                  BindingResult result,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<ProductCart> cartItems = productCartService.getCartItemsByCustomerId(loggedInUser.getId());
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("checkoutError", "Giỏ hàng trống không thể thanh toán.");
            return "redirect:/cart";
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", total);


        if (result.hasErrors()) {
            return "checkout";
        }


        try {
            orderService.createOrder(order, loggedInUser, cartItems);
            redirectAttributes.addFlashAttribute("orderSuccessMessage", "Đặt hàng thành công! Cảm ơn bạn đã mua hàng.");
            return "redirect:/home";
        } catch (IllegalArgumentException e) {
            model.addAttribute("checkoutError", e.getMessage());
            return "checkout";
        } catch (Exception e) {
            model.addAttribute("checkoutError", "Đã có lỗi xảy ra trong quá trình đặt hàng. Vui lòng thử lại.");
            e.printStackTrace();
            return "checkout";
        }
    }
}