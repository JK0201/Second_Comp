package com.comp.stock.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUserNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int restock;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_user_notification_id")
    private ProductUserNotification productUserNotification;

    public ProductUserNotificationHistory(Product product, ProductUserNotification productUserNotification) {
        this.userId = productUserNotification.getUserId();
        this.restock = product.getRestock();
        this.product = product;
        this.productUserNotification = productUserNotification;
        this.createdAt = LocalDateTime.now();
    }
}
