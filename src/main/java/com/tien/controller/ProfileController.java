package com.tien.controller;

import com.tien.dto.CustomerUpdateDTO;
import com.tien.model.Customer;
import com.tien.model.Order;
import com.tien.service.CustomerService;
import com.tien.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    private void loadProfileData(Model model, Customer loggedInUser, int currentPage, int pageSize) {
        List<Order> orders = orderService.getOrdersByCustomerId(loggedInUser.getId(), currentPage, pageSize);
        long totalOrders = orderService.countOrdersByCustomerId(loggedInUser.getId());
        long totalPages = (totalOrders + pageSize - 1) / pageSize;

        model.addAttribute("customer", loggedInUser);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);

        if (!model.containsAttribute("customerForm")) {
            model.addAttribute("customerForm", new CustomerUpdateDTO(loggedInUser.getEmail(), loggedInUser.getPhoneNumber()));
        }
    }


    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model,
                              @RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "size", defaultValue = "5") int pageSize) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        loadProfileData(model, loggedInUser, page, pageSize);
        return "profile"; // View profile.html
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("customerForm") @Valid CustomerUpdateDTO customerUpdateDTO,
                                BindingResult result,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        if (!loggedInUser.getEmail().equalsIgnoreCase(customerUpdateDTO.getEmail()) &&
                customerService.isEmailTakenByOtherUser(customerUpdateDTO.getEmail(), loggedInUser.getId())) {
            result.rejectValue("email", "error.email.taken", "Email này đã được sử dụng bởi một tài khoản khác.");
        }


        if (result.hasErrors()) {
            loadProfileData(model, loggedInUser, 1, 5); // Tải lại dữ liệu cho trang profile
            model.addAttribute("customerForm", customerUpdateDTO); // Giữ lại DTO có lỗi để hiển thị
            return "profile";
        }

        loggedInUser.setEmail(customerUpdateDTO.getEmail());
        loggedInUser.setPhoneNumber(customerUpdateDTO.getPhoneNumber());

        customerService.updateCustomer(loggedInUser); // customerService.updateCustomer sẽ gọi merge

        session.setAttribute("loggedInUser", loggedInUser); // Cập nhật session với thông tin mới
        redirectAttributes.addFlashAttribute("updateSuccessMessage", "Thông tin cá nhân đã được cập nhật thành công!");
        return "redirect:/profile";
    }

    @GetMapping("/profile/cancel-order")
    public String cancelOrder(@RequestParam("orderId") int orderId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            orderService.cancelOrder(orderId, loggedInUser.getId());
            redirectAttributes.addFlashAttribute("cancelSuccessMessage", "Đơn hàng đã được hủy thành công.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("cancelErrorMessage", e.getMessage());
        } catch (Exception e) {
            // Log lỗi ở đây
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("cancelErrorMessage", "Đã có lỗi xảy ra khi hủy đơn hàng.");
        }
        return "redirect:/profile";
    }
}