package ca.lsuderman.watertracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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
        addDailyResult();
        resetCups();
        //createNotificationWorker();

        return Result.success();
    }

    private void addDailyResult(){
        List<DailyResult> results = new ArrayList<>();
        Cup lastCup = new Cup();

//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
//        Calendar date = Calendar.getInstance();
//        date.set(Calendar.DATE, -1);
//        String yesterday = dateFormat.format(date.getTime());
        LocalDate today = LocalDate.now();
        String yesterday = (today.minusDays(1)).format(DateTimeFormatter.ISO_DATE);


        try {
            results = ((WaterDB) getApplicationContext()).getAllResults();
            lastCup = ((WaterDB) getApplicationContext()).getCup(8); //TODO: Change to amount user chooses
        } catch (Exception ex) {
            // no-op
        }
        if (results != null){
            if (lastCup.getIsDone()){
                try {
                    ((WaterDB) getApplicationContext()).addResult(yesterday, 1);
                } catch (Exception ex){
                    // no-op
                }
            } else {
                try {
                    ((WaterDB) getApplicationContext()).addResult(yesterday, 0);
                } catch (Exception ex){
                    // no-op
                }
            }
        }
    }

    private void resetCups(){
        try {
            ((WaterDB) getApplicationContext()).resetAllCups();
        } catch (Exception ex){
            // no-op
        }
    }

    private void createNotificationWorker(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String wakeUpTimeString = sharedPreferences.getString("wakeUp", "8");
        String bedtimeString = sharedPreferences.getString("bedtime", "23");

        int wakeUpTime = Integer.parseInt(wakeUpTimeString);
        int bedtime = Integer.parseInt(bedtimeString);

        int hoursAwake = bedtime - wakeUpTime;
        double timeBetweenCupsDouble = ((double)hoursAwake / 8) * 3600000;
        long timeBetweenCups = (long)timeBetweenCupsDouble; //TODO: Change to amount user chooses

        long delay = Utilities.getInitialDelay(wakeUpTime);

        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(NextCupNotificationWorker.class, timeBetweenCups, TimeUnit.MILLISECONDS)
                        //.setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInitialDelay(6, TimeUnit.HOURS)
                        .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork("next_cup_notification",
                ExistingPeriodicWorkPolicy.REPLACE,
                request);
    }
}
