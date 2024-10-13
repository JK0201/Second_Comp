package com.comp.stock.controller;

import com.comp.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final StockService stockService;

//    @PostMapping("/products/{productId}/notifications/re-stock")
//    public EntityResponse<String> sendNotification(@PathVariable Long productId) {
//        return stockService.sendNotification();
//    }
//
//    @PostMapping("/admin/products/{productId}/notifications/re-stock")
//    public EntityResponse<String> sendNotificationManually(@PathVariable Long productId) {
//        return stockService.sendNotificationManually();
//    }
}
