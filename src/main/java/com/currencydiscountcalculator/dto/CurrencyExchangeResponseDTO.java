package com.currencydiscountcalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeResponseDTO {

    private String base;
    private Map<String, BigDecimal> rates;
}
