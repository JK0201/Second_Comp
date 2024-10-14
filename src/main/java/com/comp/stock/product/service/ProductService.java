package com.comp.stock.product.service;

import com.comp.stock.notification.product_notification.entity.ProductNotificationHistory;
import com.comp.stock.product.entity.Product;
import com.comp.stock.entity.Status;
import com.comp.stock.notification.product_notification.repository.ProductNotificationHistoryRepository;
import com.comp.stock.product.repository.ProductRepository;
import com.comp.stock.notification.product_user_notification.repository.ProductUserNotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductNotificationHistoryRepository productNotificationHistoryRepository;
    private final ProductUserNotificationRepository productUserNotificationRepository;
    private final ProductRepository productRepository;

    public void addProduct(String name) {
        Product product = new Product(0, 0);
        Status status = Status.COMPLETED;
        ProductNotificationHistory productNotificationHistory = new ProductNotificationHistory(status, product);
        productNotificationHistoryRepository.save(productNotificationHistory);
    }

    @Transactional
    public void reStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

        product.restockProduct(quantity);
    }
}
