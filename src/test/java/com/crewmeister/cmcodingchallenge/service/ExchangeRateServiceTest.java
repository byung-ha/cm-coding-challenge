package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ExchangeRateServiceTest {
    @Autowired
    ExchangeRateService exchangeRateService;

    @MockBean
    ExchangeRateRepository exchangeRateRepository;

    @MockBean
    DataManageService dataManageService;

    @Test
    public void getAllExchangeRateTest() {
        Currency currency = Currency.USD;
        List<ExchangeRate> expectedResult = List.of(
                new ExchangeRate(currency, LocalDate.of(2022, 1, 1), 1.1),
                new ExchangeRate(currency, LocalDate.of(2022, 1, 2), 1.2),
                new ExchangeRate(currency, LocalDate.of(2022, 1, 3), 1.3)
        );
        when(exchangeRateRepository.findAllByCurrency(currency)).thenReturn(expectedResult);

        List<ExchangeRate> allExchangeRate = exchangeRateService.getAllExchangeRate(currency);
        assertEquals(allExchangeRate, expectedResult);
    }

    @Test
    public void getExchangeRateTest() {
        Currency currency = Currency.USD;
        LocalDate localDate = LocalDate.of(2022, 1, 1);
        double expectedExchangeRate = 1.1;
        when(exchangeRateRepository.findExchangeRate(currency, localDate)).thenReturn(expectedExchangeRate);

        Double exchangeRate = exchangeRateService.getExchangeRate(currency, localDate);
        assertEquals(expectedExchangeRate, exchangeRate);
    }

    @Test
    public void getTest() {
        Currency currency = Currency.USD;
        LocalDate localDate = LocalDate.of(2022, 1, 1);
        double fxAmount = 1000;
        double exchangeRate = 100;
        double expectedEuro = fxAmount / exchangeRate;
        when(exchangeRateRepository.findExchangeRate(currency, localDate)).thenReturn(exchangeRate);

        Double euro = exchangeRateService.convertToEuro(currency, localDate, fxAmount);
        assertEquals(expectedEuro, euro);
    }
}
