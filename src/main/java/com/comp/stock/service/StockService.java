package com.comp.stock.service;

import com.comp.stock.entity.NotificationHistory;
import com.comp.stock.entity.Product;
import com.comp.stock.entity.Status;
import com.comp.stock.entity.UserNotification;
import com.comp.stock.repository.NotificationHistoryRepository;
import com.comp.stock.repository.ProductRepository;
import com.comp.stock.repository.UserNotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final NotificationHistoryRepository notificationHistoryRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final ProductRepository productRepository;

    public void addProduct(String name) {
        Product product = new Product(name, 0, 0);
        Status status = Status.CANCELED_BY_SOLD_OUT;
        NotificationHistory notificationHistory = new NotificationHistory(status, product);
        notificationHistoryRepository.saveAndFlush(notificationHistory);
    }

    @Transactional
    public void addNotification(Long userId, Long productId) {
        Product product = productRepository.findByIdWithFetchJoin(productId);
        UserNotification userNotification = new UserNotification(userId, product);

        if (userNotificationRepository.existsByUserIdAndProductId(userId, productId)) {
            userNotificationRepository.deleteByUserIdAndProductId(userId, productId);
        } else {
            userNotificationRepository.saveAndFlush(userNotification);
        }
    }
}
