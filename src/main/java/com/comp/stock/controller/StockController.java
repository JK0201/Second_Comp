package com.comp.stock.controller;

import com.comp.stock.dto.NotificationRequestDto;
import com.comp.stock.dto.ProductRequestDto;
import com.comp.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class StockController {

    private final StockService stockService;

    @PostMapping("/add_product")
    public void addProduct(@RequestBody ProductRequestDto req) {
        stockService.addProduct(req.getName());
    }

    @PostMapping("/add_notification")
    public void addNotification(@RequestBody NotificationRequestDto req) {
        stockService.addNotification(req.getUserId(), req.getProductId());
    }
}
