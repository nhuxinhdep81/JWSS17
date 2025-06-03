package com.tien.controller;

import com.tien.model.Customer;
import com.tien.model.Product;
import com.tien.model.ProductCart;
import com.tien.service.ProductCartService;
import com.tien.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ProductCartService productCartService;

    @Autowired
    private ProductService productService;

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<ProductCart> cartItems = productCartService.getCartItemsByCustomerId(loggedInUser.getId());
        double total = cartItems.stream()
                .mapToDouble(item -> {
                    Product product = item.getProduct();
                    return (product != null && product.getPrice() != null ? product.getPrice() : 0) * item.getQuantity();
                })
                .sum();


        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart"; // View cart.html
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") int productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            productCartService.addToCart(loggedInUser.getId(), productId, quantity);
            redirectAttributes.addFlashAttribute("addSuccessMessage", "Sản phẩm đã được thêm vào giỏ hàng!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("addErrorMessage", e.getMessage());
        }

        String referer = session.getServletContext().getContextPath() + "/product/" + productId; // Hoặc lấy từ request header
        return "redirect:" + (referer != null ? referer : "/home");
    }


    @PostMapping("/cart/update")
    public String updateCart(@RequestParam("cartId") int cartId,
                             @RequestParam("quantity") int quantity,
                             HttpSession session, RedirectAttributes redirectAttributes) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            productCartService.updateCartItemQuantity(loggedInUser.getId(), cartId, quantity);
            redirectAttributes.addFlashAttribute("updateSuccessMessage", "Số lượng sản phẩm đã được cập nhật.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("updateErrorMessage", e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/remove")
    public String removeFromCart(@RequestParam("cartId") int cartId, HttpSession session, RedirectAttributes redirectAttributes) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        try {
            productCartService.removeCartItem(loggedInUser.getId(), cartId);
            redirectAttributes.addFlashAttribute("removeSuccessMessage", "Sản phẩm đã được xóa khỏi giỏ hàng.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("removeErrorMessage", e.getMessage());
        }
        return "redirect:/cart";
    }
}