package com.comp.stock.product.controller;

import com.comp.stock.dto.NotificationRequestDto;
import com.comp.stock.dto.ProductRequestDto;
import com.comp.stock.dto.RestockRequestDto;
import com.comp.stock.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add_product")
    public void addProduct(@RequestBody ProductRequestDto req) {
        productService.addProduct(req.getName());
    }

    @PostMapping("/restock_product")
    public void reStock(@RequestBody RestockRequestDto req) {
        productService.reStock(req.getProductId(), req.getQuantity());
    }
}
