package com.comp.stock.notification.product_notification.controller;

import com.comp.stock.notification.product_notification.service.ProductNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequiredArgsConstructor
public class ProductNotificationController {

    private final ProductNotificationService productNotificationService;

    @PostMapping("/products/{productId}/notifications/re-stock")
    public void sendNotification(@PathVariable Long productId) {
        productNotificationService.sendNotifications(productId);
    }

//    @PostMapping("/admin/products/{productId}/notifications/re-stock")
//    public void sendNotificationManually(@PathVariable Long productId) {
//        return productNotificationService.sendNotificationManually();
//    }
}
