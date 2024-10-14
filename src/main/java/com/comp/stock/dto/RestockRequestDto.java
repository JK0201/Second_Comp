package com.comp.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestockRequestDto {
    private Long productId;
    private int quantity;
}
