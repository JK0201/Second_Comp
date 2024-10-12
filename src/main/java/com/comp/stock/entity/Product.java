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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_history_id")
    private NotificationHistory notificationHistory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<UserNotification> userNotificationList = new ArrayList<>();

    public Product(String name, int quantity, int restock, NotificationHistory notificationHistory) {
        this.name = name;
        this.quantity = quantity;
        this.restock = restock;
        this.notificationHistory = notificationHistory;
    }

    public void restockProduct(int quantity) {
        this.quantity += quantity;
        this.restock++;
    }
}
