package com.comp.stock.notification.product_user_notification.repository;

import com.comp.stock.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
    ProductUserNotification findByUserIdAndProductId(Long userId, Long productId);
}
