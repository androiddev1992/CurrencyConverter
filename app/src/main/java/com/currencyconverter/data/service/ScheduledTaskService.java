package com.currencyconverter.data.service;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

public class ScheduledTaskService extends GcmTaskService {

    public static final String TAG = ScheduledTaskService.class.getSimpleName();

    @Override
    public int onRunTask(TaskParams taskParams) {
        startTheSyncService();
        return GcmNetworkManager.RESULT_SUCCESS;
    }

    private void startTheSyncService() {
        startService(new Intent(this, FetchCurrencyRatesService.class));
    }

    /**
     * Schedule the service if Google Play Services are available. If they are missing,
     * the {@link #onInitializeTasks() onInitializeTasks} will be called when the are installed.
     */
    public static boolean schedule(@NonNull final Context context) {

        long interval = AlarmManager.INTERVAL_HALF_HOUR;

        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (result == ConnectionResult.SUCCESS) {
            final Task syncTask = new PeriodicTask.Builder()
                    .setTag(ScheduledTaskService.TAG)
                    .setService(ScheduledTaskService.class)
                    .setPeriod(interval)
                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    .setRequiresCharging(false)
                    .setUpdateCurrent(true)
                    .build();

            GcmNetworkManager.getInstance(context).schedule(syncTask);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onInitializeTasks() {
        schedule(this);
        super.onInitializeTasks();
    }

}