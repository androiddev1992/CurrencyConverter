package com.currencyconverter.data.service;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by Varun on 21/07/17.
 */

public class ScheduledTaskService extends GcmTaskService {

    public static final String TAG = ScheduledTaskService.class.getSimpleName();

    public static final String GCM_REPEAT_TAG = "repeat|[7200,1800]";

    @Override
    public void onInitializeTasks() {
        //called when app is updated to a new version, reinstalled etc.
        //you have to schedule your repeating tasks again
        super.onInitializeTasks();

    }

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
    public static void scheduleRepeat(Context context) {

        long interval = AlarmManager.INTERVAL_HALF_HOUR;

        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (result == ConnectionResult.SUCCESS) {

            //in this method, single Repeating task is scheduled (the target service that will be called is ScheduledTaskService.class)
            try {
                PeriodicTask periodic = new PeriodicTask.Builder()
                        //specify target service - must extend GcmTaskService
                        .setService(ScheduledTaskService.class)
                        //repeat every 30 mins
                        .setPeriod(interval)
                        //specify how much earlier the task can be executed (in seconds)
                        .setFlex(60)
                        //tag that is unique to this task (can be used to cancel task)
                        .setTag(GCM_REPEAT_TAG)
                        //whether the task persists after device reboot
                        .setPersisted(true)
                        //if another task with same tag is already scheduled, replace it with this task
                        .setUpdateCurrent(true)
                        //set required network state, this line is optional
                        .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                        //request that charging must be connected, this line is optional
                        .setRequiresCharging(false)
                        .build();
                GcmNetworkManager.getInstance(context).schedule(periodic);
                Log.v(TAG, "repeating task scheduled");
            } catch (Exception e) {
                Log.e(TAG, "scheduling failed");
                e.printStackTrace();
            }
        }
    }
}