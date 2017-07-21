package com.currencyconverter.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.currencyconverter.model.CurrencyExchangeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun on 21/07/17.
 */

public class DBHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    public static void addConversionRatesToTable(String currencyKey, Float currencyRate) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(TableConversionRates.COL_CURRENCY_KEY, currencyKey);
        contentValues.put(TableConversionRates.COL_CONVERSION_VALUE, currencyRate);

        CPWrapper.insert(TableConversionRates.TABLE_NAME, contentValues);
    }

    public static List<CurrencyExchangeData> getCurrencyRatesList(String baseCurrency) {

        List<CurrencyExchangeData> exchangeRatesList = new ArrayList<>();
        Cursor cursor = null;
        try {

            cursor = CPWrapper.query(TableConversionRates.TABLE_NAME, null,  TableConversionRates.COL_CURRENCY_KEY + " != ?",
                    new String[]{baseCurrency},
                    TableConversionRates.COL_CURRENCY_KEY + " ASC");

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    CurrencyExchangeData exchangeRates = new CurrencyExchangeData();

                    String currencyName = cursor.getString(cursor.getColumnIndex(TableConversionRates.COL_CURRENCY_KEY));
                    Float exchangeRate = cursor.getFloat(cursor.getColumnIndex(TableConversionRates.COL_CONVERSION_VALUE));

                    exchangeRates.setCurrencyName(currencyName);
                    exchangeRates.setCurrencyExchangeRate(exchangeRate);

                    exchangeRatesList.add(exchangeRates);
                }
            }

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
        } finally {

            close(cursor);
        }

        // return list
        return exchangeRatesList;
    }

    public static int getConversionRatesCount() {

        int count = 0;
        Cursor cursor = null;

        try {
            cursor = CPWrapper.query(TableConversionRates.TABLE_NAME, null, null, null, null);

            if (cursor != null)
                count = cursor.getCount();

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
        } finally {

            close(cursor);
        }

        return count;
    }

    public static void deleteAllFromTableConversionRates() {

        CPWrapper.EmptyTable(TableConversionRates.TABLE_NAME);
    }

    public static void close(Cursor cursor) {

        if (cursor != null && !cursor.isClosed())
            cursor.close();
    }

}
