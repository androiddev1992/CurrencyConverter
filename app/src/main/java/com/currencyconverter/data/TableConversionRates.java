package com.currencyconverter.data;

/**
 * Created by Varun on 22/07/17.
 */

public class TableConversionRates {

    public static String TABLE_NAME = "conversion_rates";

    public static final String COL_ID = "_id";
    public static final String COL_CURRENCY_KEY = "currency_key";
    public static final String COL_CONVERSION_VALUE = "conversion_value";

    public static final String CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_CURRENCY_KEY + " VARCHAR NOT NULL," +
            COL_CONVERSION_VALUE + " INTEGER NOT NULL );";

}
