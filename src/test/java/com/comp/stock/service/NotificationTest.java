package com.comp.stock.service;

import com.comp.stock.notification.product_user_notification.service.ProductUserNotificationService;
import com.comp.stock.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class NotificationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductUserNotificationService productUserNotificationService;

    @Test
    public void 상품_1개_5000개_알림() throws Exception {
        //given
        Long productId = 1L;
        int quantity = 100;
        productService.addProduct("product1");
        productService.reStock(productId, quantity);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            Long userId = (long) (i + 1);
            executorService.submit(() -> {
                try {
                    productUserNotificationService.addNotification(userId, productId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        //when

        //then
    }
}