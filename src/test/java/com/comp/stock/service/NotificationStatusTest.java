package com.comp.stock.service;

import com.comp.stock.notification.product_notification.service.ProductNotificationService;
import com.comp.stock.notification.product_user_notification.service.ProductUserNotificationService;
import com.comp.stock.product.entity.Product;
import com.comp.stock.entity.Status;
import com.comp.stock.product.service.ProductService;
import com.comp.stock.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NotificationStatusTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductNotificationService productNotificationService;

    @Autowired
    private ProductUserNotificationService productUserNotificationService;

    @BeforeEach
    public void before() throws Exception {
        productService.addProduct("product1");
        productService.addProduct("product2");
        productService.addProduct("product3");
    }

    @AfterEach
    public void after() throws Exception {
        productRepository.deleteAll();
    }

    @Test
    public void 알림_전송시_상태() throws Exception {
        //given
        Long productId1 = 1L;
        Long productId2 = 2L;
        Long productId3 = 3L;
        Long userId = 1L;
        int quantity = 100;

        // product1
        productUserNotificationService.addNotification(userId, productId1);

        // product2
        productService.reStock(productId2, quantity);

        // product3
        productService.reStock(productId3, quantity);
        productUserNotificationService.addNotification(userId, productId3);

        //when
        productNotificationService.sendNotifications(productId1);
        productNotificationService.sendNotifications(productId2);
        productNotificationService.sendNotifications(productId3);

        //then
        Product foundProduct1 = productRepository.findByIdWithFetchJoin(productId1);
        Product foundProduct2 = productRepository.findByIdWithFetchJoin(productId2);
        Product foundProduct3 = productRepository.findByIdWithFetchJoin(productId3);

        // product1
        assertEquals(foundProduct1.getId(), productId1);
        assertEquals(foundProduct1.getRestock(), 0);
        assertEquals(foundProduct1.getQuantity(), 0);
        assertEquals(foundProduct1.getProductUserNotificationList().size(), 1);
        assertEquals(foundProduct1.getProductNotificationHistory().getStatus(), Status.CANCELED_BY_SOLD_OUT);

        assertEquals(foundProduct2.getId(), productId2);
        assertEquals(foundProduct2.getRestock(), 1);
        assertEquals(foundProduct2.getQuantity(), 100);
        assertEquals(foundProduct2.getProductUserNotificationList().size(), 0);
        assertEquals(foundProduct2.getProductNotificationHistory().getStatus(), Status.COMPLETED);

        assertEquals(foundProduct3.getId(), productId3);
        assertEquals(foundProduct3.getRestock(), 1);
        assertEquals(foundProduct3.getQuantity(), 100);
        assertEquals(foundProduct3.getProductUserNotificationList().size(), 1);
        assertEquals(foundProduct3.getProductNotificationHistory().getStatus(), Status.IN_PROGRESS);
    }
}
