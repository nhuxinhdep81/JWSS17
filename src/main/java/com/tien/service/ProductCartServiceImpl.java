package com.tien.service;

import com.tien.model.Product;
import com.tien.model.ProductCart;
import com.tien.repository.ProductCartRepository;
import com.tien.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductCartServiceImpl implements ProductCartService {

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductCart addToCart(int customerId, int productId, int quantity) throws IllegalArgumentException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }

        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại.");
        }
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Không đủ hàng tồn kho cho sản phẩm: " + product.getProductName());
        }

        ProductCart existingCartItem = productCartRepository.findByCustomerIdAndProductId(customerId, productId);
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantity;
            if (product.getStock() < newQuantity) {
                throw new IllegalArgumentException("Không đủ hàng tồn kho cho sản phẩm: " + product.getProductName() + " với tổng số lượng yêu cầu.");
            }
            existingCartItem.setQuantity(newQuantity);
            return productCartRepository.saveOrUpdate(existingCartItem);
        } else {
            ProductCart newCartItem = new ProductCart();
            newCartItem.setCustomerId(customerId);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            return productCartRepository.saveOrUpdate(newCartItem);
        }
    }

    @Override
    public List<ProductCart> getCartItemsByCustomerId(int customerId) {
        return productCartRepository.findByCustomerId(customerId);
    }

    @Override
    public ProductCart updateCartItemQuantity(int customerId, int cartId, int quantity) throws IllegalArgumentException {
        if (quantity <= 0) { // Nếu muốn xóa khi quantity = 0, thì xử lý ở removeCartItem
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0. Để xóa, vui lòng sử dụng chức năng xóa.");
        }
        ProductCart cartItem = productCartRepository.findByIdAndCustomerId(cartId, customerId);
        if (cartItem == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại trong giỏ hàng của bạn.");
        }
        Product product = productRepository.findById(cartItem.getProduct().getId());
        if (product == null) { // Hiếm khi xảy ra nếu DB nhất quán
            throw new IllegalArgumentException("Sản phẩm liên quan không tồn tại.");
        }
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Không đủ hàng tồn kho cho sản phẩm: " + product.getProductName());
        }
        cartItem.setQuantity(quantity);
        return productCartRepository.saveOrUpdate(cartItem);
    }

    @Override
    public void removeCartItem(int customerId, int cartId) throws IllegalArgumentException {
        ProductCart cartItem = productCartRepository.findByIdAndCustomerId(cartId, customerId);
        if (cartItem == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại trong giỏ hàng của bạn để xóa.");
        }
        productCartRepository.delete(cartItem);
    }

    @Override
    public void clearCart(int customerId) {
        productCartRepository.deleteAllByCustomerId(customerId);
    }
}