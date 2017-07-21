package com.currencyconverter.data.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.currencyconverter.data.DBHelper;
import com.currencyconverter.model.ExchangeRates;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Varun on 21/07/17.
 */

public class FetchCurrencyRatesService extends IntentService {

    private static final String TAG = FetchCurrencyRatesService.class.getSimpleName();

    public static final String BASE_CURRENCY = "USD";

    public FetchCurrencyRatesService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        fetchUpdatedCurrencyRates();
    }

    public void fetchUpdatedCurrencyRates() {

        try {
            GetApiInterface getApiInterface = ApiClient.getInstance().getService(GetApiInterface.class);

            Call<ExchangeRates> call = getApiInterface.fetchTrendingFeeds(BASE_CURRENCY);

            ExchangeRates response = call.execute().body();

            if (response == null || response.getRates() == null || response.getRates().size() == 0)
                return;

            DBHelper.deleteAllFromTableConversionRates();

            DBHelper.addConversionRatesToTable(BASE_CURRENCY, 1.0f);

            for (Map.Entry<String, Float> entry : response.getRates().entrySet()) {

                Log.d(TAG, "Varun " + entry.getKey() + "   " + entry.getValue());

                DBHelper.addConversionRatesToTable(entry.getKey(), entry.getValue());
            }

        } catch (IOException e) {

            Log.e(TAG, e.getMessage());

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
        }
    }
}
