package com.comp.stock.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNotificationHistory extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_history_id")
    private NotificationHistory notificationHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_notification_id")
    private UserNotification userNotification;

    public UserNotificationHistory (NotificationHistory notificationHistory, UserNotification userNotification) {
        this.notificationHistory = notificationHistory;
        this.userNotification = userNotification;
    }
}
