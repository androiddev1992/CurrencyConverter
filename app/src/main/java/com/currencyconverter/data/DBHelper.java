package com.currencyconverter.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

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
