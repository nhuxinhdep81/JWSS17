package com.tien.controller;

import com.tien.model.Product;
import com.tien.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping({"/", "/home"})
    public String homePage(@RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "size", defaultValue = "6") int pageSize, // Tăng số sản phẩm/trang
                           Model model) {

        List<Product> products = productService.getProductsPaginated(page, pageSize);
        long totalProducts = productService.countTotalProducts();
        long totalPages = (totalProducts + pageSize - 1) / pageSize;

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        return "home"; // View home.html
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable("id") int id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/home?productNotFound=true";
        }
        model.addAttribute("product", product);
        return "product-detail";
    }
}