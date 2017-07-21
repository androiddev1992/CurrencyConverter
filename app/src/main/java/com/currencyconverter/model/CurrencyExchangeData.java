package com.currencyconverter.model;

import java.io.Serializable;

/**
 * Created by Varun on 22/07/17.
 */

public class CurrencyExchangeData implements Serializable {

    private String currencyName;

    private Float currencyExchangeRate;

    /**
     *  Getters
     */

    public String getCurrencyName() {
        return currencyName;
    }

    public Float getCurrencyExchangeRate() {
        return currencyExchangeRate;
    }

    /**
     *  Setters
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setCurrencyExchangeRate(Float currencyExchangeRate) {
        this.currencyExchangeRate = currencyExchangeRate;
    }
}
