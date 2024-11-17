package com.currencydiscountcalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO {
    private List<ItemDTO> items;
    private BigDecimal totalAmount;
    private UserType userType;
    private int customerTenureYears;
    private String originalCurrency;
    private String targetCurrency;
    private BigDecimal percentageDiscount;
    private BigDecimal discountsOnEveryHundredUSD;
    private BigDecimal totalPayableAmount;
    private BigDecimal totalDiscount;
}
