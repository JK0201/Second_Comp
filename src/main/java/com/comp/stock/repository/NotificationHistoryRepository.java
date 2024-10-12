package com.comp.stock.repository;

import com.comp.stock.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
}
