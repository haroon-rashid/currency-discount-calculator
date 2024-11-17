package com.currencydiscountcalculator.service;


import com.currencydiscountcalculator.dto.BillRequestDTO;
import com.currencydiscountcalculator.dto.BillResponseDTO;
import com.currencydiscountcalculator.dto.ItemDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class BillService {

    private final CurrencyExchangeService currencyExchangeService;
    private final DiscountService discountService;

    @Autowired
    public BillService(CurrencyExchangeService currencyExchangeService, DiscountService discountService) {
        this.currencyExchangeService = currencyExchangeService;
        this.discountService = discountService;
    }

    public ResponseEntity<BillResponseDTO> calculateConvertedAmount(BillRequestDTO billRequestDTO) {

        validateTotalAmount(billRequestDTO);
        /* Get the exchange rates for the original currency */
        Map<String, BigDecimal> conversionRates = currencyExchangeService.getExchangeRates(billRequestDTO.getOriginalCurrency());

        if (!conversionRates.containsKey(billRequestDTO.getTargetCurrency())) {
            throw new RuntimeException("Target currency not found in exchange rates");
        }
        BillResponseDTO billResponseDTO  = new BillResponseDTO();

        /* apply discount when $5 per $100 block */
        BigDecimal additionalDiscount = discountService.applyDiscountsOnEveryHundredUSD(billRequestDTO, conversionRates);
        BigDecimal conversionRate = conversionRates.get(billRequestDTO.getTargetCurrency());
        BigDecimal convertedAmount = billRequestDTO.getTotalAmount().multiply(conversionRate);

        /* apply the percentage-based discounts */
        BigDecimal percentageDiscount = discountService.applyDiscounts(billRequestDTO, additionalDiscount, conversionRate);
        BigDecimal allDiscount = percentageDiscount.add(additionalDiscount);
        BigDecimal totalDiscounts = allDiscount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalAmountAfterDiscounts = convertedAmount.subtract(totalDiscounts);

        BeanUtils.copyProperties(billRequestDTO,billResponseDTO);
        billResponseDTO.setDiscountsOnEveryHundredUSD(additionalDiscount);
        billResponseDTO.setTotalPayableAmount(finalAmountAfterDiscounts.setScale(2, RoundingMode.HALF_UP));
        billResponseDTO.setPercentageDiscount(percentageDiscount);
        billResponseDTO.setTotalDiscount(totalDiscounts);
        return ResponseEntity.ok().body(billResponseDTO);
    }

    public void validateTotalAmount(BillRequestDTO request) {
        BigDecimal totalItemsAmount = request.getItems().stream()
                .map(ItemDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (request.getTotalAmount().compareTo(totalItemsAmount) != 0) {
            throw new IllegalArgumentException("Total amount does not match the sum of item prices.");
        }
    }
}