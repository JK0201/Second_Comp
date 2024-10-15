package com.comp.stock.service;

import com.comp.stock.notification.product_notification.service.ProductNotificationService;
import com.comp.stock.notification.product_user_notification.service.ProductUserNotificationService;
import com.comp.stock.product.service.ProductService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Disabled
public class NotificationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductUserNotificationService productUserNotificationService;
    @Autowired
    private ProductNotificationService productNotificationService;

    @Test
    public void 상품_알림() throws Exception {
        //given
        Long productId = 1L;
        int quantity = 100;
        createProductAndRestock(productId, quantity);

        int threadCount = 5000;
        ExecutorService executorService = Executors.newFixedThreadPool(500);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            Long userId = (long) (i + 1);
            // 발송 여부 On / Off가 섞여있을 경우
//            int active = (int) Math.round(Math.random());
            int active = 1;
            executorService.submit(() -> {
                try {
                    productUserNotificationService.addNotification(userId, active, productId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        //when
        long startTime = System.currentTimeMillis();
        productNotificationService.sendNotifications(productId);
        long endTime = System.currentTimeMillis();

        //then
        Long expectedTime = (long) (threadCount / 500 * 1000);
        assertTrue(expectedTime <= (endTime - startTime), "수행 시간이 초당 500건의 결과인지 확인");
    }

    private void createProductAndRestock(Long productId, int quantity) {
        productService.addProduct("product1");
        productService.reStock(productId, quantity);
    }
}