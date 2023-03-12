package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.property.BundesBankApiProperty;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ExchangeRateControllerTest {
    public static final Currency TEST_CURRENCY = Currency.USD;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DataManageController dataManageController;
    @MockBean
    ExchangeRateService exchangeRateService;
    @MockBean
    BundesBankApiProperty bundesBankApiProperty;

    @Test
    void getListOfAvailableCurrencies() throws Exception {
        mockMvc.perform(get("/api/currencies"))
                .andExpect(content().json("[\"AUD\",\"BGN\",\"BRL\",\"CAD\",\"CHF\",\"CNY\",\"CYP\",\"CZK\",\"DKK\",\"EEK\",\"GBP\",\"GRD\",\"HKD\",\"HRK\",\"HUF\",\"IDR\",\"ILS\",\"INR\",\"ISK\",\"JPY\",\"KRW\",\"LTL\",\"LVL\",\"MTL\",\"MXN\",\"MYR\",\"NOK\",\"NZD\",\"PHP\",\"PLN\",\"ROL\",\"RON\",\"RUB\",\"SEK\",\"SGD\",\"SIT\",\"SKK\",\"THB\",\"TRL\",\"TRY\",\"USD\",\"ZAR\",\"EUR\"]"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllCurrencyData() throws Exception {
        when(exchangeRateService.getAllExchangeRate(TEST_CURRENCY))
                .thenReturn(List.of(new ExchangeRate(TEST_CURRENCY, LocalDate.now().minusDays(2), 1.2)));
        mockMvc.perform(get("/api/{currency}", TEST_CURRENCY.name()))
                .andExpect(jsonPath("$[0].rate").value(1.2))
                .andExpect(status().isOk());
    }
    @Test
    void getAllCurrencyDataWithWrongCurrency() throws Exception {
        mockMvc.perform(get("/api/XXX"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getCurrencyData() throws Exception {
        when(exchangeRateService.getAllExchangeRate(TEST_CURRENCY))
                .thenReturn(List.of(new ExchangeRate(TEST_CURRENCY, LocalDate.now().minusDays(2), 1.2)));
        mockMvc.perform(get("/api/{currency}", TEST_CURRENCY.name()))
                .andExpect(jsonPath("$[0].rate").value(1.2))
                .andExpect(status().isOk());
    }

    @Test
    void getExchangeRate() throws Exception {
        LocalDate localDate = LocalDate.of(2022, 01, 01);
        double rate = 1.2;
        when(exchangeRateService.getExchangeRate(TEST_CURRENCY, localDate)).thenReturn(rate);
        when(bundesBankApiProperty.getStartDate()).thenReturn(LocalDate.of(2021, 01, 01));
        mockMvc.perform(get("/api/{currency}/{date}", TEST_CURRENCY.name(), localDate))
                .andExpect(content().json("1.2"))
                .andExpect(status().isOk());
    }

    @Test
    void getExchangeRateWithWrongDate() throws Exception {
        LocalDate wrongDate = LocalDate.of(1980, 1, 1);
        when(bundesBankApiProperty.getStartDate()).thenReturn(LocalDate.of(2021, 01, 01));
        mockMvc.perform(get("/api/{currency}/{date}", TEST_CURRENCY.name(), wrongDate))
                .andExpect(content().string("It's not supported to get data before 2021-01-01."))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getConvertedEuro() throws Exception {
        LocalDate localDate = LocalDate.of(2022, 01, 01);
        double fxAmount = 1000;
        double expectedEuroAmount = 900;

        when(exchangeRateService.convertToEuro(TEST_CURRENCY, localDate, fxAmount)).thenReturn(expectedEuroAmount);
        when(bundesBankApiProperty.getStartDate()).thenReturn(LocalDate.of(2021, 01, 01));
        mockMvc.perform(get("/api/{currency}/{date}/{amount}", TEST_CURRENCY.name(), localDate, fxAmount))
                .andExpect(content().json("900.0"))
                .andExpect(status().isOk());
    }

    @Test
    void getConvertedEuroWithWrongAmount() throws Exception {
        LocalDate localDate = LocalDate.of(2022, 01, 01);
        double fxAmount = 1000;
        double expectedEuroAmount = 900;

        when(exchangeRateService.convertToEuro(TEST_CURRENCY, localDate, fxAmount)).thenReturn(expectedEuroAmount);
        when(bundesBankApiProperty.getStartDate()).thenReturn(LocalDate.of(2021, 01, 01));

        mockMvc.perform(get("/api/{currency}/{date}/0", TEST_CURRENCY.name(), localDate))
                .andExpect(content().json("0.0"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/{currency}/{date}/XXX", TEST_CURRENCY.name(), localDate))
                .andExpect(status().is4xxClientError());

    }
}
