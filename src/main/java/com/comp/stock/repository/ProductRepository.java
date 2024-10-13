package com.comp.stock.repository;

import com.comp.stock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.id = :productId")
    Product findByIdWithOutFetchJoin(Long productId);

    @Query("select p from Product p " +
            "join fetch p.notificationHistory " +
            "left join fetch p.userNotificationList " +
            "where p.id = :productId")
    Product findByIdWithFetchJoin(@Param("productId") Long productId);
}
