package com.comp.stock.entity;

import com.comp.stock.notification.product_notification.entity.ProductNotificationHistory;
import com.comp.stock.product.entity.Product;
import com.comp.stock.notification.product_notification.repository.ProductNotificationHistoryRepository;
import com.comp.stock.product.repository.ProductRepository;
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
    private ProductNotificationHistoryRepository productNotificationHistoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager em;

    private Long productId;

    @BeforeEach
    public void before() throws Exception {
        int restock = 0;
        int quantity = 0;
        Status status = Status.COMPLETED;

        // Product와 Notification Hisotry를 초기화 및 저장
        Product product = new Product(quantity, restock);
        ProductNotificationHistory productNotificationHistory = new ProductNotificationHistory(status, product);
        productNotificationHistoryRepository.saveAndFlush(productNotificationHistory);

        productId = product.getId();
    }

    @AfterEach
    public void after() throws Exception {
        productNotificationHistoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void 상품_초기화_테스트() throws Exception {
        //given
        int expectedRestock = 0;
        int expectedQuantity = 0;
        Status expectedStatus = Status.COMPLETED;

        //when && then
        Product foundProduct = productRepository.findById(productId).orElseThrow();
        assertEquals(foundProduct.getQuantity(), expectedQuantity);
        assertEquals(foundProduct.getRestock(), expectedRestock);
        assertEquals(foundProduct.getProductNotificationHistory().getStatus(), expectedStatus);
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