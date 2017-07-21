package com.currencyconverter.data.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.currencyconverter.data.DatabaseContract;
import com.currencyconverter.model.ExchangeRates;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Varun on 21/07/17.
 */

public class FetchCurrencyRatesService extends IntentService {

    private static final String TAG = FetchCurrencyRatesService.class.getSimpleName();

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

            Call<ExchangeRates> call = getApiInterface.fetchTrendingFeeds("USD");

            ExchangeRates response = call.execute().body();

            if (response == null || response.getRates() == null || response.getRates().size() == 0)
                return;

            for (Map.Entry<String, Long> entry : response.getRates().entrySet()) {

                ContentValues contentValues = new ContentValues();

                contentValues.put(DatabaseContract.TableConversionRates.COL_CURRENCY_KEY, entry.getKey());
                contentValues.put(DatabaseContract.TableConversionRates.COL_CONVERSION_VALUE, entry.getValue());

                getContentResolver().insert(DatabaseContract.CONTENT_URI, contentValues);
            }

        } catch (IOException e) {

            Log.e(TAG, e.getMessage());

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
        }
    }
}
