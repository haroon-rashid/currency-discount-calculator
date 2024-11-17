package com.currencydiscountcalculator.service;

import com.currencydiscountcalculator.dto.BillRequestDTO;
import com.currencydiscountcalculator.dto.BillResponseDTO;
import com.currencydiscountcalculator.dto.ItemDTO;
import com.currencydiscountcalculator.dto.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {

    @InjectMocks
    private BillService billService;

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @Mock
    private DiscountService discountService;

    private BillRequestDTO billRequestDTO;
    private Map<String, BigDecimal> exchangeRates;

    @BeforeEach
    void setup() {
        billRequestDTO = new BillRequestDTO();
        billRequestDTO.setOriginalCurrency("USD");
        billRequestDTO.setTargetCurrency("AED");
        billRequestDTO.setTotalAmount(BigDecimal.valueOf(200));
        billRequestDTO.setItems(List.of(new ItemDTO("item1", "non-grocery", BigDecimal.valueOf(100)), new ItemDTO("item2", "grocery", BigDecimal.valueOf(100))));
        billRequestDTO.setUserType(UserType.EMPLOYEE);
        billRequestDTO.setCustomerTenureYears(3);
        exchangeRates = new HashMap<>();
        exchangeRates.put("AED", BigDecimal.valueOf(3.67));
        exchangeRates.put("USD", BigDecimal.ONE);
    }

    @Test
    void testCalculateConvertedAmount_ValidRequest() {
        when(currencyExchangeService.getExchangeRates("USD")).thenReturn(exchangeRates);
        when(discountService.applyDiscountsOnEveryHundredUSD(any(), any())).thenReturn(BigDecimal.valueOf(10));
        when(discountService.applyDiscounts(any(), any(), any())).thenReturn(BigDecimal.valueOf(15));
        ResponseEntity<?> response = billService.calculateConvertedAmount(billRequestDTO);
        assertNotNull(response);
        BillResponseDTO responseBody = (BillResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals(BigDecimal.valueOf(709.00).setScale(2), responseBody.getTotalPayableAmount());
        assertEquals(BigDecimal.valueOf(10), responseBody.getDiscountsOnEveryHundredUSD());
        assertEquals(BigDecimal.valueOf(15), responseBody.getPercentageDiscount());
    }

    @Test
    void testCalculateConvertedAmount_TargetCurrencyNotFound() {
        exchangeRates.remove("AED");
        when(currencyExchangeService.getExchangeRates("USD")).thenReturn(exchangeRates);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            billService.calculateConvertedAmount(billRequestDTO);
        });
        assertEquals("Target currency not found in exchange rates", exception.getMessage());
    }

    @Test
    void testCalculateConvertedAmount_InvalidTotalAmount() {
        billRequestDTO.setTotalAmount(BigDecimal.valueOf(150));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            billService.calculateConvertedAmount(billRequestDTO);
        });
        assertEquals("Total amount does not match the sum of item prices.", exception.getMessage());
    }

    @Test
    void testValidateTotalAmount_ValidTotal() {
        assertDoesNotThrow(() -> billService.validateTotalAmount(billRequestDTO));
    }

    @Test
    void testValidateTotalAmount_InvalidTotal() {
        billRequestDTO.setTotalAmount(BigDecimal.valueOf(150));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            billService.validateTotalAmount(billRequestDTO);
        });
        assertEquals("Total amount does not match the sum of item prices.", exception.getMessage());
    }
}
