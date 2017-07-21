package com.currencyconverter.data;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Varun on 21/07/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    public static final String DB_NAME = "currency_conversion.db";
    public static final int DB_VERSION = 1;

    private static final String SQL_CREATE_TABLE_CONVERSION_RATES = "CREATE TABLE " +
            DatabaseContract.TABLE_CONVERSION_RATES + " (" +
            DatabaseContract.TableConversionRates._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DatabaseContract.TableConversionRates.COL_CURRENCY_KEY + " TEXT NOT NULL," +
            DatabaseContract.TableConversionRates.COL_CONVERSION_VALUE + " TEXT NOT NULL )";

    private Resources mResources;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_TABLE_CONVERSION_RATES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_CONVERSION_RATES);
        onCreate(db);
    }

}
