package com.comp.stock.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int restock;

    @Column(nullable = false)
    private Long recentUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductNotificationHistory(Status status,int restock, Long recentUserId, Product product) {
        this.restock = restock;
        this.recentUserId = recentUserId;
        this.status = status;
        this.product = product;
    }

    public void setRestock(int restock) {
        this.restock = restock;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setRecentUserId(Long userId) {
        this.recentUserId = userId;
    }
}
