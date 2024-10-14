package com.comp.stock.service;

import com.comp.stock.notification.product_user_notification.entity.ProductUserNotification;
import com.comp.stock.notification.product_user_notification.service.ProductUserNotificationService;
import com.comp.stock.product.entity.Product;
import com.comp.stock.product.repository.ProductRepository;
import com.comp.stock.product.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

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
    public void 유저_알림_추가() throws Exception {
        //given
        Long productId1 = 1L;
        Long productId2 = 2L;
        Long productId3 = 3L;

        //when
        int threadCount = 3000;
        ExecutorService executorService = Executors.newFixedThreadPool(300);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < 1000; i++) {
            Long userId = (long) (i + 1);
            executorService.submit(() -> {
                addNotification(latch, userId, productId1);
                addNotification(latch, userId, productId2);
                addNotification(latch, userId, productId3);
            });
        }

        latch.await();
        executorService.shutdown();

        //then
        checkAssertions(productId1);
        checkAssertions(productId2);
        checkAssertions(productId3);
    }

    private void addNotification(CountDownLatch latch, Long userId, Long productId) {
        try {
            productUserNotificationService.addNotification(userId, productId);
        } finally {
            latch.countDown();
        }
    }

    private void checkAssertions(Long productId) {
        int expectedSize = 1000;

        Product foundProduct = productRepository.findByIdWithFetchJoin(productId);
        List<ProductUserNotification> productUserNotificationList = foundProduct.getProductUserNotificationList();

        assertEquals(foundProduct.getId(), productId);
        assertEquals(productUserNotificationList.size(), expectedSize);
    }
}