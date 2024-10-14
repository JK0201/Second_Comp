package com.comp.stock.notification.product_notification.service;

import com.comp.stock.entity.*;
import com.comp.stock.notification.product_notification.entity.ProductNotificationHistory;
import com.comp.stock.notification.product_notification.entity.ProductUserNotificationHistory;
import com.comp.stock.notification.product_user_notification.entity.ProductUserNotification;
import com.comp.stock.product.entity.Product;
import com.comp.stock.notification.product_notification.repository.ProductNotificationHistoryRepository;
import com.comp.stock.product.repository.ProductRepository;
import com.comp.stock.notification.product_notification.repository.ProductUserNotificationHistoryRepository;
import com.comp.stock.notification.product_user_notification.repository.ProductUserNotificationRepository;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductNotificationService {

    private final ConcurrentLinkedDeque<ProductUserNotification> notificationDeque = new ConcurrentLinkedDeque<>();
    private final RateLimiter rateLimiter = RateLimiter.create(1000);

    private final ProductNotificationHistoryRepository productNotificationHistoryRepository;
    private final ProductUserNotificationRepository productUserNotificationRepository;
    private final ProductRepository productRepository;
    private final ProductUserNotificationHistoryRepository productUserNotificationHistoryRepository;

    @Transactional
    public void sendNotifications(Long productId) {
        Product product = productRepository.findByIdWithFetchJoin(productId);

        if (product.getProductNotificationHistory().getStatus() == Status.CANCELED_BY_ERROR) {
            updateStatus(product, Status.IN_PROGRESS);
            processNotifications(product);
            return;
        } else if (product.getQuantity() <= 0) {
            // 재고가 없을 경우
            updateStatus(product, Status.CANCELED_BY_SOLD_OUT);
            return;
        } else if (product.getProductUserNotificationList().isEmpty()) {
            // 재고는 있지만 알림 설정이 없을 경우
            updateStatus(product, Status.COMPLETED);
            return;
        }

        // 재고가 있고 알림 설정도 있을 경우
        updateStatus(product, Status.IN_PROGRESS);

        List<ProductUserNotification> notificationList = product.getProductUserNotificationList();
        notificationList.forEach(notificationDeque::offer);

        processNotifications(product);
        updateStatus(product, Status.COMPLETED);
    }

    public void processNotifications(Product product) {
        while (!notificationDeque.isEmpty()) {
            rateLimiter.acquire();
            long currentTime = System.currentTimeMillis();

            ProductUserNotification notification = notificationDeque.pollFirst();
            if (notification != null) {
                sendNotification(notification);
                saveNotificationHistory(product, notification.getUserId());
                productUserNotificationRepository.delete(notification);
            }
        }
    }

    private void sendNotification(ProductUserNotification notification) {
        log.info("Notification sent to userId : " + notification.getUserId());
    }

    private void saveNotificationHistory(Product product, Long userId) {
        ProductUserNotificationHistory history = new ProductUserNotificationHistory(product, userId);
        productUserNotificationHistoryRepository.save(history);
    }

    private void updateStatus(Product product, Status status) {
        ProductNotificationHistory history = product.getProductNotificationHistory();
        history.setStatus(status);
    }
}
