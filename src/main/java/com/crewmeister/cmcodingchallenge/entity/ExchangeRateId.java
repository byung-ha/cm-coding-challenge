package com.crewmeister.cmcodingchallenge.entity;

import com.crewmeister.cmcodingchallenge.model.Currency;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;

public class ExchangeRateId implements Serializable {
    @Id
    private Currency currency;
    @Id
    private LocalDate date;

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
}
