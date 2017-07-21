package com.currencyconverter;

import android.app.Application;

public class CurrencyConverterApplication extends Application {

    // Global Instance, used by application
    private static CurrencyConverterApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static synchronized CurrencyConverterApplication getInstance() {
        return sInstance;
    }

}
