package com.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Spinner;

import com.currencyconverter.data.DBHelper;
import com.currencyconverter.data.service.FetchCurrencyRatesService;
import com.currencyconverter.data.service.ScheduledTaskService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etConversionValue)
    TextInputEditText etConversionValue;

    @BindView(R.id.spinnerBaseCurrency)
    Spinner spinnerBaseCurrency;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        startService(new Intent(this, FetchCurrencyRatesService.class));

        if (DBHelper.getConversionRatesCount() == 0) {

            /** Used the GcmTaskService instead of JobScheduler cause
             *  JobScheduler is not supported by pre-lollipop versions **/

            ScheduledTaskService.scheduleRepeat(this);
        }

        initViews();
    }

    private void initViews() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerview.setLayoutManager(gridLayoutManager);

        recyclerview.setHasFixedSize(false);

        CurrencyRateAdapter adapter = new CurrencyRateAdapter(this, DBHelper.getCurrencyRatesList(FetchCurrencyRatesService.BASE_CURRENCY));
        recyclerview.setAdapter(adapter);
    }
}
