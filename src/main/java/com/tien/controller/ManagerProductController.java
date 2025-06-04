package com.tien.controller;

import com.tien.model.Product;
import com.tien.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ManagerProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "3") int pageSize,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            Model model) {
        List<Product> products;
        long totalProducts;
        long totalPages;

        // Nếu có minPrice hoặc maxPrice, lọc theo giá
        if (minPrice != null && maxPrice != null) {
            products = productService.getProductsByPriceRangePaginated(minPrice, maxPrice, page, pageSize);
            totalProducts = productService.countProductsByPriceRange(minPrice, maxPrice);
        } else {
            products = productService.getProductsPaginated(page, pageSize);
            totalProducts = productService.countTotalProducts();
        }

        totalPages = (totalProducts + pageSize - 1) / pageSize;

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "manager_product";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "manager_product_form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product, Model model) {
        productService.saveProduct(product);
        return "redirect:/admin/products?saveSuccess=true";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/admin/products?productNotFound=true";
        }
        model.addAttribute("product", product);
        return "manager_product_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products?deleteSuccess=true";
    }
}