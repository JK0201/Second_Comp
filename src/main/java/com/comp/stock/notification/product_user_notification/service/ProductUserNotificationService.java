package com.comp.stock.notification.product_user_notification.service;

import com.comp.stock.notification.product_user_notification.entity.ProductUserNotification;
import com.comp.stock.notification.product_user_notification.repository.ProductUserNotificationRepository;
import com.comp.stock.product.entity.Product;
import com.comp.stock.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductUserNotificationService {

    private final ProductRepository productRepository;
    private final ProductUserNotificationRepository productUserNotificationRepository;

    @Transactional
    public void addNotification(Long userId, int active, Long productId) {
        ProductUserNotification existUser = productUserNotificationRepository.findByUserIdAndProductId(userId, productId);

        if (existUser != null) {
            if (existUser.getActive() != active) {
                existUser.setActive(active);
            }
            return;
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재 하지 않습니다."));
        ProductUserNotification productUserNotification = new ProductUserNotification(userId, active, product);
        productUserNotificationRepository.save(productUserNotification);
    }
}