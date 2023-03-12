package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDto;
import com.crewmeister.cmcodingchallenge.exception.NotSupportedDateException;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.property.BundesBankApiProperty;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api")
public class ExchangeRateController {
    private static final Logger LOGGER = Logger.getLogger(ExchangeRateController.class.getName());
    private final ExchangeRateService exchangeRateService;
    private final BundesBankApiProperty bankApiProperty;

    public ExchangeRateController(ExchangeRateService exchangeRateService, BundesBankApiProperty bankApiProperty) {
        this.exchangeRateService = exchangeRateService;
        this.bankApiProperty = bankApiProperty;
    }

    @GetMapping("/currencies")
    public Currency[] getCurrencies() {
        return Currency.values();
    }

    @GetMapping("/{currency}")
    public List<ExchangeRateDto> getAllExchangeRate(@PathVariable Currency currency) {
        return exchangeRateService.getAllExchangeRate(currency).stream()
                .map(er -> new ExchangeRateDto(er))
                .collect(Collectors.toList());
    }

    @GetMapping("/{currency}/{date}")
    public Double getExchangeRate(@PathVariable Currency currency, @PathVariable LocalDate date) {
        validateDateParam(date);
        return exchangeRateService.getExchangeRate(currency, date);
    }

    @GetMapping("/{fromCurrency}/{date}/{amount}")
    public Double convertToEuro(@PathVariable Currency fromCurrency, @PathVariable LocalDate date, @PathVariable Double amount) {
        validateDateParam(date);
        return exchangeRateService.convertToEuro(fromCurrency, date, amount);
    }

    @GetMapping("/{fromCurrency}/{date}/{amount}/{toCurrency}")
    public Double convertToCurrency(@PathVariable Currency fromCurrency, @PathVariable LocalDate date, @PathVariable double amount, @PathVariable Currency toCurrency) {
        validateDateParam(date);
        return exchangeRateService.convertToCurrency(fromCurrency, toCurrency, date, amount);
    }

    private void validateDateParam(LocalDate date) {
        if (date.isBefore(bankApiProperty.getStartDate()))
            throw new NotSupportedDateException(String.format("It's not supported to get data before %s.", bankApiProperty.getStartDate()));
    }

}