package com.comp.stock.repository;

import com.comp.stock.entity.UserNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationHistoryRepository extends JpaRepository<UserNotificationHistory, Long> {
}
