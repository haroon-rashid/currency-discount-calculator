package com.currencydiscountcalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private String name;  // Item name (e.g., "Apple")
    private String category;  // Item category (e.g., "Grocery", "Electronics")
    private BigDecimal price;  // Price of the item
}