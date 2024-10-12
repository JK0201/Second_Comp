package com.comp.stock.entity;

import com.comp.stock.repository.ProductRepository;
import com.comp.stock.repository.UserNotificationRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserNotificationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private EntityManager em;

    private Long productId;

    @BeforeEach
    void before() throws Exception {
        String name = "product1";
        int quantity = 0;
        int restock = 0;

        NotificationHistory notificationHistory = new NotificationHistory(Status.COMPLETED);

        // Product와 Notification Hisotry를 저장
        Product product = new Product(name, quantity, restock, notificationHistory);
        productRepository.saveAndFlush(product);

        productId = product.getId();
    }

    @AfterEach
    void after() throws Exception {
        userNotificationRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @Transactional
    void 상품_재입고_알림o() throws Exception {
        //given
        int quantity = 100;
        Long userId = 1L;

        Product notificationProduct = productRepository.findById(productId).orElseThrow();
        UserNotification userNotification = new UserNotification(userId, notificationProduct);
        userNotificationRepository.save(userNotification);
        em.flush();
        em.clear();

        //when
        // Product와 Notification Hisotry, User Notification을 함께 가져옴
        Product product = productRepository.findByIdWithNotificationHistory(productId);
        NotificationHistory notificationHistory = product.getNotificationHistory();
        List<UserNotification> userNotificationList = product.getUserNotificationList();

        Status status;
        // 조건에 따라서 Notification History 값 변경
        if (userNotificationList.isEmpty()) status = Status.COMPLETED;
        else status = Status.IN_PROGRESS;

        notificationHistory.setStatus(status);
        product.restockProduct(quantity);
        em.flush();
        em.clear();

        //then
        Product foundProduct = productRepository.findById(productId).orElseThrow();
        Long expectedProductId = 1L;
        int expectedQuantity = 100;
        int expectedRestock = 1;
        Status expectedStatus = Status.IN_PROGRESS;
        Long expectedUserId = 1L;

        assertEquals(foundProduct.getId(), expectedProductId);
        assertEquals(foundProduct.getQuantity(), expectedQuantity);
        assertEquals(foundProduct.getRestock(), expectedRestock);
        assertEquals(foundProduct.getNotificationHistory().getStatus(), expectedStatus);
        assertEquals(foundProduct.getUserNotificationList().get(0).getUserId(), expectedUserId);
    }
}