package com.comp.stock.service;

import com.comp.stock.entity.Product;
import com.comp.stock.entity.UserNotification;
import com.comp.stock.repository.ProductRepository;
import com.comp.stock.repository.UserNotificationRepository;
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
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @BeforeEach
    public void before() throws Exception {
        stockService.addProduct("product1");
        stockService.addProduct("product2");
        stockService.addProduct("product3");
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
        int threadCount = 900;
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < 300; i++) {
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
            stockService.addNotification(userId, productId);
        } finally {
            latch.countDown();
        }
    }

    private void checkAssertions(Long productId) {
        int expectedSize = 300;

        Product foundProduct = productRepository.findByIdWithFetchJoin(productId);
        List<UserNotification> userNotificationList = foundProduct.getUserNotificationList();

        assertEquals(foundProduct.getId(), productId);
        assertEquals(userNotificationList.size(), expectedSize);
    }
}