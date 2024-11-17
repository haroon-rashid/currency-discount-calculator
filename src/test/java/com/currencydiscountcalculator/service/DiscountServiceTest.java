package com.currencydiscountcalculator.service;

import com.currencydiscountcalculator.dto.BillRequestDTO;
import com.currencydiscountcalculator.dto.ItemDTO;
import com.currencydiscountcalculator.dto.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountServiceTest {

    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();
    }

    @Test
    void testApplyDiscountsWithNonGroceryItems() {
        BillRequestDTO request = new BillRequestDTO();
        request.setUserType(UserType.EMPLOYEE);
        request.setItems(Collections.singletonList(new ItemDTO("Laptop", "Electronics", new BigDecimal("1000"))));
        request.setCustomerTenureYears(1);
        BigDecimal conversionRate = BigDecimal.ONE;
        BigDecimal additionalDiscount = BigDecimal.ZERO;
        BigDecimal discount = discountService.applyDiscounts(request, additionalDiscount, conversionRate);
        assertEquals(new BigDecimal("300"), discount.setScale(0, RoundingMode.UP));
    }

    @Test
    void testApplyDiscountsWithCustomerTenure() {

        BillRequestDTO request = new BillRequestDTO();
        request.setUserType(UserType.CUSTOMER);
        request.setItems(Collections.singletonList(new ItemDTO("Laptop", "Electronics", new BigDecimal("1000"))));
        request.setCustomerTenureYears(3);
        BigDecimal conversionRate = BigDecimal.ONE;
        BigDecimal additionalDiscount = BigDecimal.ZERO;
        BigDecimal discount = discountService.applyDiscounts(request, additionalDiscount, conversionRate);
        assertEquals(new BigDecimal("50"), discount.setScale(0, RoundingMode.UP));
    }

    @Test
    void testApplyDiscountsWithNoNonGroceryItems() {
        BillRequestDTO request = new BillRequestDTO();
        request.setUserType(UserType.CUSTOMER);
        request.setItems(Collections.singletonList(new ItemDTO("Apple", "Grocery", new BigDecimal("200"))));
        request.setCustomerTenureYears(1);
        BigDecimal conversionRate = BigDecimal.ONE;
        BigDecimal additionalDiscount = BigDecimal.ZERO;
        BigDecimal discount = discountService.applyDiscounts(request, additionalDiscount, conversionRate);
        assertEquals(BigDecimal.ZERO, discount.setScale(0, RoundingMode.UP));
    }

    @Test
    void testApplyDiscountsOnEveryHundredUSD() {
        BillRequestDTO request = new BillRequestDTO();
        request.setTotalAmount(new BigDecimal("500"));
        request.setTargetCurrency("AED");
        Map<String, BigDecimal> conversionRates = Map.of("USD", new BigDecimal("0.27"), "AED", BigDecimal.ONE);
        BigDecimal additionalDiscount = discountService.applyDiscountsOnEveryHundredUSD(request, conversionRates);
        assertEquals(new BigDecimal("18.52"), additionalDiscount.setScale(2, RoundingMode.UP));
    }
}
