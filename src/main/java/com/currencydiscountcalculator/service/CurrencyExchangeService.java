package com.currencydiscountcalculator.service;

import com.currencydiscountcalculator.constants.Constants;
import com.currencydiscountcalculator.dto.ExchangeRateResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;


@Slf4j
@Service
public class CurrencyExchangeService {

    private final WebClient.Builder webClientBuilder;
    @Value("${currency.api.base-url}")
    private String currencyApiKey;
    private WebClient webClient;

    @Autowired
    CurrencyExchangeService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.baseUrl(currencyApiKey).build();
    }


    @Cacheable(value = "exchangeRates", key = "#baseCurrency")
    public Map<String, BigDecimal> getExchangeRates(String baseCurrency) {
        log.info("getExchangeRates => Fetching rates for: {}", baseCurrency);

        try {
            ExchangeRateResponseDTO response = webClient.get().uri(baseCurrency).retrieve().bodyToMono(ExchangeRateResponseDTO.class).block();

            if (response != null && Constants.SUCCESS.equals(response.getResult())) {
                log.info("getExchangeRates => Rates fetched for: {}", baseCurrency);
                return response.getConversionRates();
            } else {
                throw new RuntimeException("Failed to fetch exchange rates");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching exchange rates: " + e.getMessage());
        }
    }
}


