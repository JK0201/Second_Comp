package com.comp.stock.notification.product_notification.service;

import com.comp.stock.entity.*;
import com.comp.stock.notification.product_notification.repository.ProductUserNotificationHistoryRepository;
import com.comp.stock.product.repository.ProductRepository;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductNotificationService {

    private final ConcurrentLinkedDeque<ProductUserNotification> notificationDeque = new ConcurrentLinkedDeque<>();
    // 초당 500건 제한
    private final RateLimiter rateLimiter = RateLimiter.create(500, 1, TimeUnit.SECONDS);

    private final ProductRepository productRepository;
    private final ProductUserNotificationHistoryRepository productUserNotificationHistoryRepository;

    @Transactional
    public void sendNotifications(Long productId) {
        Product product = productRepository.findByIdAndActive(productId);

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

            // Queue에 저장된 순서대로 알림을 보냄
            ProductUserNotification notification = notificationDeque.pollFirst();
            if (notification != null) {
                sendNotification(notification);
                saveNotificationHistory(product, notification);
            }
        }
    }

    private void sendNotification(ProductUserNotification notification) {
        log.info("Notification sent to userId : " + notification.getUserId());
    }

    private void saveNotificationHistory(Product product, ProductUserNotification notification) {
        product.getProductNotificationHistory().setRecentUserId(notification.getUserId());
        ProductUserNotificationHistory history = new ProductUserNotificationHistory(product, notification);
        productUserNotificationHistoryRepository.save(history);
    }

    private void updateStatus(Product product, Status status) {
        ProductNotificationHistory history = product.getProductNotificationHistory();
        history.setStatus(status);
    }
}
