package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.property.BundesBankApiProperty;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class DataManageService {
    private static final Logger LOGGER = Logger.getLogger(DataManageService.class.getName());
    private final ExchangeRateRepository exchangeRateRepository;

    private final RestTemplate restTemplate;
    private final BundesBankApiProperty bundesBankApiProperty;
    private final HttpEntity request;

    public DataManageService(ExchangeRateRepository exchangeRateRepository, BundesBankApiProperty bundesBankApiProperty, RestTemplateBuilder restTemplateBuilder) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.bundesBankApiProperty = bundesBankApiProperty;
        this.restTemplate = restTemplateBuilder.build();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(bundesBankApiProperty.getBaseUrl()));


        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.bbk.data+csv");
        this.request = new HttpEntity(headers);
    }

    public void loadAllCurrencyData() {
        Arrays.stream(Currency.values()).forEach(currency -> loadCurrencyData(currency));
    }

    public void loadCurrencyData(Currency currency) {
        loadCurrencyData(currency, bundesBankApiProperty.getStartDate());
    }
    public void loadCurrencyData(Currency currency, LocalDate startDate) {
        List<ExchangeRate> exchangeRateList = Arrays.stream(restTemplate.exchange(
                        "/data/BBEX3/{refId}/?startPeriod={startPeriod}",
                        HttpMethod.GET,
                        request,
                        String.class,
                        currency.getRefId(),
                        startDate
                ).getBody().split("\n"))
                .map(line -> parseExchangeRate(currency, line))
                .filter(exchangeRate -> exchangeRate != null)
                .collect(Collectors.toList());
        exchangeRateRepository.saveAll(exchangeRateList);
    }

    private ExchangeRate parseExchangeRate(Currency currency, String line) {
        String[] split = line.split(";");
        try {
            return new ExchangeRate(currency, LocalDate.parse(split[0]), parseExchangeRate(split[1]));
        } catch (DateTimeParseException e) {
        }
        return null;
    }

    private Double parseExchangeRate(String s) {
        if (s.equals("."))
            return null;
        else
            return Double.parseDouble(s.replace(",", "."));
    }
}
