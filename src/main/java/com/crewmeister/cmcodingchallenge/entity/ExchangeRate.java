package com.crewmeister.cmcodingchallenge.entity;

import com.crewmeister.cmcodingchallenge.model.Currency;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.time.LocalDate;

@Entity
@IdClass(ExchangeRateId.class)
public class ExchangeRate {
    @Id
    private Currency currency;
    @Id
    private LocalDate date;

    @Nullable
    private Double exchangeRate;

    public ExchangeRate() {

    }

    public ExchangeRate(Currency currency, LocalDate date, Double exchangeRate) {
        this.currency = currency;
        this.date = date;
        this.exchangeRate = exchangeRate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
