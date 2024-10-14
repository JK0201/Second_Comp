package com.comp.stock.notification.product_user_notification.controller;

import com.comp.stock.dto.NotificationRequestDto;
import com.comp.stock.notification.product_user_notification.service.ProductUserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductUserNotificationController {

    private final ProductUserNotificationService productNotificationService;

    @PostMapping("/add_notification")
    public void addNotification(@RequestBody NotificationRequestDto req) {
        productNotificationService.addNotification(req.getUserId(), req.getActive(), req.getProductId());
    }
}
