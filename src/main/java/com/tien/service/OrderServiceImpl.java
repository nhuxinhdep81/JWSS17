package com.tien.service;

import com.tien.model.Customer;
import com.tien.model.Order;
import com.tien.model.Product;
import com.tien.model.ProductCart;
import com.tien.repository.OrderRepository;
import com.tien.repository.ProductRepository;
import com.tien.repository.ProductCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCartRepository productCartRepository;

    @Override
    public Order createOrder(Order orderDetails, Customer customer, List<ProductCart> cartItems) throws IllegalArgumentException {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng trống. Không thể tạo đơn hàng.");
        }

        for (ProductCart item : cartItems) {
            Product product = productRepository.findById(item.getProduct().getId());
            if (product == null || product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Sản phẩm " + (product != null ? product.getProductName() : "ID "+ item.getProduct().getId()) + " không đủ hàng hoặc không tồn tại.");
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.update(product);
        }

        Order newOrder = new Order();
        newOrder.setCustomerId(customer.getId());
        newOrder.setRecipientName(orderDetails.getRecipientName());
        newOrder.setPhoneNumber(orderDetails.getPhoneNumber());
        newOrder.setAddress(orderDetails.getAddress());

        newOrder.setListProduct(cartItems.stream().map(cart -> cart.getProduct().getId()).collect(Collectors.toList()));

        double totalMoney = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        newOrder.setTotalMoney(totalMoney);
        newOrder.setStatus("PENDING");

        Order savedOrder = orderRepository.save(newOrder);

        productCartRepository.deleteAllByCustomerId(customer.getId());

        return savedOrder;
    }

    @Override
    public List<Order> getOrdersByCustomerId(int customerId, int page, int size) {
        return orderRepository.findByCustomerId(customerId, page, size);
    }

    @Override
    public long countOrdersByCustomerId(int customerId) {
        return orderRepository.countByCustomerId(customerId);
    }

    @Override
    public void cancelOrder(int orderId, int customerId) throws IllegalArgumentException {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId);
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng hoặc bạn không có quyền thực hiện thao tác này.");
        }
        if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalArgumentException("Chỉ có thể hủy đơn hàng đang ở trạng thái 'Đang chờ xử lý'.");
        }

        for (Integer productId : order.getListProduct()) {
            Product product = productRepository.findById(productId);
            if (product != null) {
            }
        }

        order.setStatus("CANCELLED");
        orderRepository.update(order);
    }

    @Override
    public Order getOrderByIdAndCustomerId(int orderId, int customerId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId);
    }

    @Override
    public List<Order> getAllOrders(int page, int size) {
        return orderRepository.findAllPaginated(page, size);
    }

    @Override
    public long countAllOrders() {
        return orderRepository.countAll();
    }

    @Override
    public void updateOrderStatus(int orderId, String status) {
        Order order = orderRepository.findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderRepository.update(order);
        }
    }

    @Override
    public Map<String, Long> countOrdersByStatus() {
        Map<String, Long> statusCounts = new HashMap<>();
        String[] statuses = {"PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"};
        for (String status : statuses) {
            statusCounts.put(status, orderRepository.countByStatus(status));
        }
        return statusCounts;
    }

    @Override
    public double getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }

    @Override
    public List<Order> searchOrdersByRecipientNameAndStatus(String recipientName, String status, int page, int size) {
        return orderRepository.findByRecipientNameAndStatusPaginated(recipientName, status, page, size);
    }

    @Override
    public long countOrdersByRecipientNameAndStatus(String recipientName, String status) {
        return orderRepository.countByRecipientNameAndStatus(recipientName, status);
    }
}