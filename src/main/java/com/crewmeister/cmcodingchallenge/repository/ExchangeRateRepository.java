package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.entity.ExchangeRateId;
import com.crewmeister.cmcodingchallenge.model.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public
interface ExchangeRateRepository extends CrudRepository<ExchangeRate, ExchangeRateId> {
    List<ExchangeRate> findAllByCurrency(Currency currency);

    @Query("SELECT er.exchangeRate FROM ExchangeRate er WHERE er.currency= :currency and er.date<=:date and er.exchangeRate IS NOT NULL order by er.date desc limit 1")
    Double findExchangeRate(Currency currency, LocalDate date);

    ExchangeRate findFirstByCurrencyOrderByDateDesc(Currency currency);
}
