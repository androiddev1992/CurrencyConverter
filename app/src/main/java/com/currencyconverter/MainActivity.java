package com.currencyconverter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.currencyconverter.data.DBHelper;
import com.currencyconverter.data.service.FetchCurrencyRatesService;
import com.currencyconverter.data.service.ScheduledTaskService;
import com.currencyconverter.model.NewCurrencyRatesReceived;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.etConversionValue)
    TextInputEditText etConversionValue;

    @BindView(R.id.spinnerBaseCurrency)
    Spinner spinnerBaseCurrency;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private CurrencyRateAdapter adapter;

    private EventBus mEventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mEventBus.register(this);

        adapter = new CurrencyRateAdapter(this, null);

        initViews();

        if (DBHelper.getConversionRatesCount() == 0) {

            startService(new Intent(this, FetchCurrencyRatesService.class));

            /** Used the GcmTaskService instead of JobScheduler cause
             *  JobScheduler is not supported by pre-lollipop versions **/
            ScheduledTaskService.scheduleRepeat(this);

        } else {
            loadValuesInSpinner();
            loadListFromDb(getValueToExchange(), getCurrencyToExchange());
        }
    }

    @Override
    protected void onDestroy() {
        mEventBus.unregister(this);
        super.onDestroy();
    }

    private void initViews() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerview.setLayoutManager(gridLayoutManager);

        recyclerview.setHasFixedSize(false);

        recyclerview.setAdapter(adapter);

        etConversionValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (etConversionValue.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter a value to convert..", Toast.LENGTH_SHORT).show();
                    } else {
                        if (adapter != null)
                            adapter.updateList(DBHelper.getCurrencyRatesList(getValueToExchange(), getCurrencyToExchange()));
                    }

                    return true;
                }
                return false;
            }
        });

        etConversionValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void loadValuesInSpinner() {

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, DBHelper.getCurrencyNamesList());

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerBaseCurrency.setAdapter(dataAdapter);

        int spinnerPosition = dataAdapter.getPosition(getCurrencyToExchange());

        //set the default according to value
        spinnerBaseCurrency.setSelection(spinnerPosition);

        spinnerBaseCurrency.setOnItemSelectedListener(this);
    }

    private void loadListFromDb(Float valueToConvert, String currency) {

        if (adapter != null)
            adapter.updateList(DBHelper.getCurrencyRatesList(valueToConvert, currency));
    }

    @Subscribe
    public void onEvent(NewCurrencyRatesReceived event) {
        loadListFromDb(getValueToExchange(), getCurrencyToExchange());

        if (spinnerBaseCurrency != null && spinnerBaseCurrency.getAdapter().getCount() == 0)
            loadValuesInSpinner();
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if (adapter != null)
            adapter.updateList(DBHelper.getCurrencyRatesList(getValueToExchange(), item));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Float getValueToExchange() {

        String value = etConversionValue.getText().toString();
        return value.length() == 0 ? FetchCurrencyRatesService.DEFAULT_VALUE : Float.parseFloat(value);
    }

    private String getCurrencyToExchange() {

        String text = spinnerBaseCurrency.getSelectedItem().toString();
        return text.length() == 0 ? FetchCurrencyRatesService.BASE_CURRENCY : text;
    }
}
