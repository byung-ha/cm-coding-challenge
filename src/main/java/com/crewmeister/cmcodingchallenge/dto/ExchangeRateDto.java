package com.crewmeister.cmcodingchallenge.dto;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;

import java.time.LocalDate;

public class ExchangeRateDto {

    private LocalDate date;

    private Double rate;

    public ExchangeRateDto(LocalDate date, Double rate) {
        this.date = date;
        this.rate = rate;
    }

    public ExchangeRateDto(ExchangeRate er) {
        this.date = er.getDate();
        this.rate = er.getExchangeRate();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
