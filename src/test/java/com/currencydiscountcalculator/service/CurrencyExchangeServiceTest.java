package com.currencydiscountcalculator.service;

import com.currencydiscountcalculator.dto.ExchangeRateResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CurrencyExchangeServiceTest {

    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private ExchangeRateResponseDTO exchangeRateResponseDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ExchangeRateResponseDTO.class)).thenReturn(Mono.just(exchangeRateResponseDTO));
    }


    @Test
    void testGetExchangeRates_Failure() {
        String baseCurrency = "USD";
        when(exchangeRateResponseDTO.getResult()).thenReturn("failure");
        when(responseSpec.bodyToMono(ExchangeRateResponseDTO.class)).thenReturn(Mono.just(exchangeRateResponseDTO));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            currencyExchangeService.getExchangeRates(baseCurrency);
        });
        assertTrue(exception.getMessage().contains("Error fetching exchange rates: "));
    }

    @Test
    void testGetExchangeRates_ExceptionHandling() {
        String baseCurrency = "USD";
        when(responseSpec.bodyToMono(ExchangeRateResponseDTO.class)).thenThrow(RuntimeException.class);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            currencyExchangeService.getExchangeRates(baseCurrency);
        });
        assertTrue(exception.getMessage().contains("Error fetching exchange rates"));
    }
}
