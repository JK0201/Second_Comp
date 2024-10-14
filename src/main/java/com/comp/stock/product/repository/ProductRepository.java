package com.comp.stock.product.repository;

import com.comp.stock.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p " +
            "join fetch p.productNotificationHistory " +
            "left join fetch p.productUserNotificationList " +
            "where p.id = :productId")
    Product findByIdWithFetchJoin(@Param("productId") Long productId);
}
