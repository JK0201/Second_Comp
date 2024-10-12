package com.comp.stock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.EntityResponse;

@Service
@RequiredArgsConstructor
public class StockService {



    public EntityResponse<String> sendNotification() {
        return null;
    }

    public EntityResponse<String> sendNotificationManually() {
        return null;
    }
}
