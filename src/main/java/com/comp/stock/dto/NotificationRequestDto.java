package com.comp.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto {
    private Long userId;
    private int active;
    private Long productId;
}
