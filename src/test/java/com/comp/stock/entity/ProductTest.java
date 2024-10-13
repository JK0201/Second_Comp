package com.comp.stock.entity;

import com.comp.stock.repository.NotificationHistoryRepository;
import com.comp.stock.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductTest {

    @Autowired
    private NotificationHistoryRepository notificationHistoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager em;

    private Long productId;

    @BeforeEach
    public void before() throws Exception {
        String name = "product1";
        int quantity = 0;
        int restock = 0;
        Status status = Status.CANCELED_BY_SOLD_OUT;

        // Product와 Notification Hisotry를 초기화 및 저장
        Product product = new Product(name, quantity, restock);
        NotificationHistory notificationHistory = new NotificationHistory(status, product);
        notificationHistoryRepository.saveAndFlush(notificationHistory);

        productId = product.getId();
    }

    @AfterEach
    public void after() throws Exception {
        notificationHistoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void 상품_초기화_테스트() throws Exception {
        //given
        String expectedName = "product1";
        int expectedQuantity = 0;
        int expectedRestock = 0;
        Status expectedStatus = Status.CANCELED_BY_SOLD_OUT;

        //when && then
        Product foundProduct = productRepository.findById(productId).orElseThrow();
        assertEquals(foundProduct.getName(), expectedName);
        assertEquals(foundProduct.getQuantity(), expectedQuantity);
        assertEquals(foundProduct.getRestock(), expectedRestock);
        assertEquals(foundProduct.getNotificationHistory().getStatus(), expectedStatus);
    }

    @Test
    @Transactional
    void 상품_재입고() throws Exception {
        //given
        int quantity = 100;
        Product product = productRepository.findById(productId).orElseThrow();

        //when
        product.restockProduct(quantity);
        em.flush();
        em.clear();

        //then
        Product foundProduct = productRepository.findById(productId).orElseThrow();
        int expectedQuantity = 100;
        int expectedRestock = 1;

        assertEquals(foundProduct.getQuantity(), expectedQuantity);
        assertEquals(foundProduct.getRestock(), expectedRestock);
    }

    @Test
    @Transactional
    public void 상품_재고_감소() throws Exception {
        //given
        int quantity = 100;
        Product product = productRepository.findById(productId).orElseThrow();
        product.restockProduct(quantity);

        //when
        product.reduceQuantity(100);
        em.flush();
        em.clear();

        //then
        int expectedQuantity = 0;

        Product foundProduct = productRepository.findById(productId).orElseThrow();
        assertEquals(foundProduct.getQuantity(), expectedQuantity);
    }
}