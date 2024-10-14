package com.comp.stock.notification.product_notification.entity;

import com.comp.stock.entity.Timestamped;
import com.comp.stock.notification.product_user_notification.entity.ProductUserNotification;
import com.comp.stock.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUserNotificationHistory extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int restock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductUserNotificationHistory(Product product, Long userId) {
        this.userId = userId;
        this.restock = product.getRestock();
        this.product = product;
    }
}
