package com.currencyconverter.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Varun on 21/07/17.
 */

public class DatabaseContract {

    // Database schema information
    public static final String TABLE_CONVERSION_RATES = "conversion_rates";

    public static final class TableConversionRates implements BaseColumns {
        public static final String COL_ID = "_id";
        public static final String COL_CURRENCY_KEY = "currency_key";
        public static final String COL_CONVERSION_VALUE = "conversion_value";
    }

    // Unique authority string for the content provider
    public static final String CONTENT_AUTHORITY = "com.currencyconverter";
    // Default sort for query results
    public static final String DEFAULT_SORT_RATES = TableConversionRates.COL_CURRENCY_KEY;

    // Base content Uri for accessing the provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_CONVERSION_RATES)
            .build();

}
