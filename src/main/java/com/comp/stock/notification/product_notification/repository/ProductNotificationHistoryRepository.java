package com.comp.stock.notification.product_notification.repository;

import com.comp.stock.entity.ProductNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {
    ProductNotificationHistory findByProductId(Long productId);
}
