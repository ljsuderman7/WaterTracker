package ca.lsuderman.watertracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class EverydayWorker extends Worker {

    private SharedPreferences sharedPreferences;

    public EverydayWorker (
            @NonNull Context context,
            @NonNull WorkerParameters params){
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        //TODO: Reset All Cups
        ((WaterDB) getApplicationContext()).resetAllCups();

        String wakeUpTimeString = sharedPreferences.getString("WakeUp", "8");
        String bedtimeString = sharedPreferences.getString("bedtime", "23");

        int wakeUpTime = Integer.parseInt(wakeUpTimeString);
        int bedtime = Integer.parseInt(bedtimeString);

        int hoursAwake = bedtime - wakeUpTime;
        int timeBetweenCups = hoursAwake / 8; //TODO: Change to amount user chooses

        long delay = Utilities.getInitialDelay(wakeUpTime);

        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(NextCupNotificationWorker.class, timeBetweenCups, TimeUnit.HOURS)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork("next_cup_notification",
                ExistingPeriodicWorkPolicy.REPLACE,
                request);

        Log.d("Testing EverydayWorker Initial Delay", "SUCCESS");
        return Result.success();
    }
}
