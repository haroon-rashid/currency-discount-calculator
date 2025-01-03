package com.currencydiscountcalculator;



import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@EnableScheduling
@EnableCaching
@SpringBootApplication
public class CurrencyDiscountCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyDiscountCalculatorApplication.class, args);
	}

	@CacheEvict(value = "exchangeRates", allEntries = true)
	@Scheduled(fixedRate = 3600000) // Every hour
	public void evictCachePeriodically() {
		log.info("===>>> Evicting All cache <<<===== ");
	}
}
