package com.currencydiscountcalculator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponseDTO {

    private String result;  // Should be "success" for valid responses
    @JsonProperty("base_code")
    private String baseCode; // Base currency, e.g., "USD"
    @JsonProperty("conversion_rates")
    private Map<String, BigDecimal> conversionRates; // Map of conversion rates
}
