package com.currencyconverter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.currencyconverter.model.CurrencyExchangeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun on 22/07/17.
 */

public class CurrencyRateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CurrencyRateAdapter.class.getSimpleName();

    private List<CurrencyExchangeData> exchangeRatesList;
    private Context context;

    public CurrencyRateAdapter(Context context, List<CurrencyExchangeData> exchangeRatesList) {
        super();

        this.context = context;
        this.exchangeRatesList = new ArrayList<>();

        if (exchangeRatesList != null && exchangeRatesList.size() > 0)
            this.exchangeRatesList.addAll(exchangeRatesList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View transactionItemView = inflater.inflate(R.layout.item_currency, viewGroup, false);
        viewHolder = new CurrencyRateHolder(transactionItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int viewPosition) {

        final int position = viewHolder.getAdapterPosition();

        final CurrencyExchangeData exchangeRate = exchangeRatesList.get(position);

        if (exchangeRate == null)
            return;

        CurrencyRateHolder holder = (CurrencyRateHolder) viewHolder;

        holder.tvCurrencyName.setText(exchangeRate.getCurrencyName());
        holder.tvExchangeValue.setText("" + exchangeRate.getCurrencyExchangeRate());

    }

    @Override
    public int getItemCount() {
        return (exchangeRatesList != null ? exchangeRatesList.size() : 0);
    }

    class CurrencyRateHolder extends RecyclerView.ViewHolder {

        public TextView tvCurrencyName, tvExchangeValue;

        public CurrencyRateHolder(View itemView) {
            super(itemView);

            tvCurrencyName = (TextView) itemView.findViewById(R.id.tvCurrencyName);
            tvExchangeValue = (TextView) itemView.findViewById(R.id.tvExchangeValue);
        }
    }

}
