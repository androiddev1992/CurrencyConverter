package com.currencyconverter.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Varun on 22/07/17.
 */

public class ExchangeRates implements Serializable{

    private String base;

    private String date;

    private HashMap<String, Float> rates;

    /**
     *  Getters
     */
    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public HashMap<String, Float> getRates() {
        return rates;
    }

    /**
     *  Setters
     */
    public void setBase(String base) {
        this.base = base;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRates(HashMap<String, Float> rates) {
        this.rates = rates;
    }
}
