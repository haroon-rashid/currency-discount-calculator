package com.currencydiscountcalculator.service;

import com.currencydiscountcalculator.constants.Constants;
import com.currencydiscountcalculator.dto.BillRequestDTO;
import com.currencydiscountcalculator.dto.ItemDTO;
import com.currencydiscountcalculator.dto.UserType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;


@Service
public class DiscountService {

    /* apply discount on percentage */
    public BigDecimal applyDiscounts(BillRequestDTO request, BigDecimal additionalDiscount, BigDecimal conversionRate) {

        BigDecimal nonGroceryAmount = request.getItems().stream().filter(item -> !Constants.GROCERY.equalsIgnoreCase(item.getCategory())).map(ItemDTO::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal percentageDiscount = BigDecimal.ZERO;
        if (nonGroceryAmount.compareTo(BigDecimal.ZERO) > 0) {
            if (request.getUserType() == UserType.EMPLOYEE) {
                percentageDiscount = nonGroceryAmount.multiply(BigDecimal.valueOf(0.30)); // 30% for employees
            } else if (request.getUserType() == UserType.AFFILIATE) {
                percentageDiscount = nonGroceryAmount.multiply(BigDecimal.valueOf(0.10)); // 10% for affiliates
            } else if (request.getCustomerTenureYears() > 2) {
                percentageDiscount = nonGroceryAmount.multiply(BigDecimal.valueOf(0.05)); // 5% for customers with tenure > 2 years
            }
        }
        return percentageDiscount.multiply(conversionRate).setScale(2, RoundingMode.HALF_UP);
    }


    /* apply discount when $5 per $100 block */
    public BigDecimal applyDiscountsOnEveryHundredUSD(BillRequestDTO request, Map<String, BigDecimal> conversionRates) {
        BigDecimal conversionRate = conversionRates.get(request.getTargetCurrency());
        BigDecimal usdRate = conversionRates.get(Constants.USD);
        BigDecimal amountInUSD = request.getTotalAmount().multiply(usdRate);
        BigDecimal additionalDiscount = BigDecimal.ZERO;
        if (amountInUSD.compareTo(BigDecimal.valueOf(100)) > 0) {
            BigDecimal hundredBlocks = amountInUSD.divide(BigDecimal.valueOf(100), 0, RoundingMode.DOWN);
            BigDecimal discount = hundredBlocks.multiply(BigDecimal.valueOf(5));
            BigDecimal discountInUSD = discount.divide(usdRate, 2, RoundingMode.UP);
            additionalDiscount = conversionRate.multiply(discountInUSD);
        }
        return additionalDiscount;

    }
}
