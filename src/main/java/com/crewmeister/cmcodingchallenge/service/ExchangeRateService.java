package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final DataManageService dataManageService;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository,
                               DataManageService dataManageService) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.dataManageService = dataManageService;
    }


    public List<ExchangeRate> getAllExchangeRate(Currency currency) {
        loadData(currency);
        return exchangeRateRepository.findAllByCurrency(currency);
    }

    private void loadData(Currency currency) {
        ExchangeRate er = exchangeRateRepository.findFirstByCurrencyOrderByDateDesc(currency);
        if(er == null)
            dataManageService.loadCurrencyData(currency);
        else if(er.getDate().isBefore(LocalDate.now())) {
            dataManageService.loadCurrencyData(currency, er.getDate().plusDays(1));
        }
    }

    public Double getExchangeRate(Currency currency, LocalDate date) {
        if(currency == Currency.EUR)
            return 1.0;
        loadData(currency);
        return exchangeRateRepository.findExchangeRate(currency, date);
    }

    public Double convertToEuro(Currency currency, LocalDate date, double amount) {
        return amount / getExchangeRate(currency, date);
    }

    public Double convertToCurrency(Currency fromCurrency, Currency toCurrency, LocalDate date, double amount) {
        return amount / getExchangeRate(fromCurrency, date) * getExchangeRate(toCurrency, date);
    }
}
