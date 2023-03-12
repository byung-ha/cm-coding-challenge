package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.service.DataManageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataManageController {
    private final DataManageService dataManageService;


    public DataManageController(DataManageService dataManageService) {
        this.dataManageService = dataManageService;
    }

    @GetMapping("/load")
    public void loadAllCurrencyData() {
        dataManageService.loadAllCurrencyData();
    }

    @GetMapping("/load/{currency}")
    public void loadCurrencyData(@PathVariable Currency currency) {
        dataManageService.loadCurrencyData(currency);
    }

}
