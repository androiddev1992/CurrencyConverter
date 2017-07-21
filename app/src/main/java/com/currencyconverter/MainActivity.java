package com.currencyconverter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.currencyconverter.data.DatabaseContract;
import com.currencyconverter.data.service.ScheduledTaskService;
import com.google.android.gms.gcm.GcmNetworkManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.etConversionValue)
    private TextInputEditText etConversionValue;

    @BindView(R.id.spinnerBaseCurrency)
    private EditText spinnerBaseCurrency;

    @BindView(R.id.recyclerview)
    private RecyclerView recyclerview;

    GcmNetworkManager gcmNetworkManager;

    public static final String TAG_PERIODIC = "PERIODIC";

    public static final String[] FLASHCARD_COLUMNS = {
            DatabaseContract.TableConversionRates.COL_ID,
            DatabaseContract.TableConversionRates.COL_CURRENCY_KEY,
            DatabaseContract.TableConversionRates.COL_CONVERSION_VALUE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        gcmNetworkManager = GcmNetworkManager.getInstance(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = DatabaseContract.DEFAULT_SORT_RATES;

        return new CursorLoader(this,
                DatabaseContract.CONTENT_URI,
                FLASHCARD_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() == 0) {

            /** Used the GcmTaskService instead of JobScheduler cause
            *  JobScheduler is not supported by pre-lollipop versions **/

            ScheduledTaskService.schedule(this);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
