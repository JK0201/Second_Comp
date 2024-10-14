package com.comp.stock.entity;

import com.comp.stock.notification.product_notification.entity.ProductNotificationHistory;
import com.comp.stock.notification.product_user_notification.entity.ProductUserNotification;
import com.comp.stock.product.entity.Product;
import com.comp.stock.notification.product_notification.repository.ProductNotificationHistoryRepository;
import com.comp.stock.product.repository.ProductRepository;
import com.comp.stock.notification.product_user_notification.repository.ProductUserNotificationRepository;
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
class ProductUserNotificationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductNotificationHistoryRepository productNotificationHistoryRepository;

    @Autowired
    private ProductUserNotificationRepository productUserNotificationRepository;

    private Long productId;

    @BeforeEach
    void before() throws Exception {
        int restock = 0;
        int quantity = 0;
        Status status = Status.COMPLETED;

        Product product = new Product(quantity, restock);
        ProductNotificationHistory productNotificationHistory = new ProductNotificationHistory(status, product);
        productNotificationHistoryRepository.saveAndFlush(productNotificationHistory);

        productId = product.getId();
    }

    @AfterEach
    void after() throws Exception {
        productUserNotificationRepository.deleteAll();
        productNotificationHistoryRepository.deleteAll();
        productRepository.deleteAll();
    }


    @Test
    @Rollback(false)
    void 유저_알림_추가() throws Exception {
        //given
        Long userId = 1L;

        //when
        Product product = productRepository.findById(productId).orElseThrow();
        List<ProductUserNotification> productUserNotificationList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductUserNotification productUserNotification = new ProductUserNotification(userId, product);
            productUserNotificationList.add(productUserNotification);
        }

        productUserNotificationRepository.saveAllAndFlush(productUserNotificationList);

        //then
        int expectedSize = 10;

        Product foundProduct = productRepository.findByIdWithFetchJoin(productId);
        List<ProductUserNotification> foundProductUserNotificationList = foundProduct.getProductUserNotificationList();

        assertEquals(foundProduct.getId(), productId);
        assertEquals(foundProductUserNotificationList.size(), expectedSize);
    }
}