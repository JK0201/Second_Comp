package com.comp.stock.entity;

import com.comp.stock.repository.NotificationHistoryRepository;
import com.comp.stock.repository.ProductRepository;
import com.comp.stock.repository.UserNotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserNotificationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationHistoryRepository notificationHistoryRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    private Long productId;

    @BeforeEach
    void before() throws Exception {
        String name = "product1";
        int quantity = 0;
        int restock = 0;
        Status status = Status.CANCELED_BY_SOLD_OUT;

        Product product = new Product(name, quantity, restock);
        NotificationHistory notificationHistory = new NotificationHistory(status, product);
        notificationHistoryRepository.saveAndFlush(notificationHistory);

        productId = product.getId();
    }

    @AfterEach
    void after() throws Exception {
        userNotificationRepository.deleteAll();
        notificationHistoryRepository.deleteAll();
        productRepository.deleteAll();
    }


    @Test
    @Rollback(false)
    void 유저_알림_추가() throws Exception {
        //given
        Long userId = 1L;

        //when
        Product product = productRepository.findById(productId).orElseThrow();
        List<UserNotification> userNotificationList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserNotification userNotification = new UserNotification(userId, product);
            userNotificationList.add(userNotification);
        }

        userNotificationRepository.saveAllAndFlush(userNotificationList);

        //then
        int expectedSize = 10;

        Product foundProduct = productRepository.findByIdWithFetchJoin(productId);
        List<UserNotification> foundUserNotificationList = foundProduct.getUserNotificationList();

        assertEquals(foundProduct.getId(), productId);
        assertEquals(foundUserNotificationList.size(), expectedSize);
    }
}