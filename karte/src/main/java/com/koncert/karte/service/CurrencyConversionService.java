package com.koncert.karte.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private final RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        String url = "https://api.exchangerate-api.com/v4/latest/" + fromCurrency;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("rates")) {
            Map<String, Double> rates = (Map<String, Double>) response.get("rates");
            Double rate = rates.get(toCurrency);
            if (rate != null) {
                return amount.multiply(BigDecimal.valueOf(rate)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        throw new RuntimeException("Ne mogu da dobijem kurs za " + toCurrency);
    }
}