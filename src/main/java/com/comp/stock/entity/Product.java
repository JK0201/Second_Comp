package com.comp.stock.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int restock;

    @OneToOne(mappedBy = "product")
    private NotificationHistory notificationHistory;

    @OneToMany(mappedBy = "product")
    private List<UserNotification> userNotificationList = new ArrayList<>();

    public Product(String name, int quantity, int restock) {
        this.name = name;
        this.quantity = quantity;
        this.restock = restock;
    }

    public void restockProduct(int quantity) {
        this.quantity += quantity;
        this.restock++;
    }

    public void reduceQuantity(int quantity) {
        if(this.quantity - quantity < 0) throw new RuntimeException("재고는 0개 미만이 될 수 없습니다.");
        this.quantity -= quantity;
    }
}
