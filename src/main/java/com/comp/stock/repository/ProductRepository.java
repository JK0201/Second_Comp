package com.comp.stock.repository;

import com.comp.stock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p " +
            "join fetch p.notificationHistory " +
            "left join fetch p.userNotificationList u " +
            "where p.id = :productId")
    Product findByIdWithNotificationHistory(@Param("productId") Long productId);
}
