package com.comp.stock.notification.product_user_notification.entity;

import com.comp.stock.entity.Timestamped;
import com.comp.stock.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUserNotification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductUserNotification(Long userId, int active, Product product) {
        this.userId = userId;
        this.active = active;
        this.product = product;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
