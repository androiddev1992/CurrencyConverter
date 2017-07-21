package com.currencyconverter.data.service;

import com.currencyconverter.model.ExchangeRates;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Varun on 21/07/17.
 */

public interface GetApiInterface {

    @GET("/latest")
    Call<ExchangeRates> fetchTrendingFeeds(@Query("base") String baseCurrency);

}