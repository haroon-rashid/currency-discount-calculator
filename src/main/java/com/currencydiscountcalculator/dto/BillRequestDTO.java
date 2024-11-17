package com.currencydiscountcalculator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillRequestDTO {
    private List<ItemDTO> items;
    private BigDecimal totalAmount;
    private UserType userType;
    private int customerTenureYears;
    private String originalCurrency;
    private String targetCurrency;
}

