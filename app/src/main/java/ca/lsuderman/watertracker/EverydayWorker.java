package ca.lsuderman.watertracker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class EverydayWorker extends Worker {
    public EverydayWorker (
            @NonNull Context context,
            @NonNull WorkerParameters params){
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        //TODO: Reset All Cups

        //TODO: Check for correct amount of cups finished by predetermined time interval
        // - Create PeriodicWorker for time interval

        // runs during the last 15 minutes of every hour
//        WorkRequest saveRequest =
//                new PeriodicWorkRequest.Builder(SaveImageToFileWorker.class,
//                        1, TimeUnit.HOURS,
//                        15, TimeUnit.MINUTES)
//                        .build();

        Log.d("Testing EverydayWorker Initial Delay", "SUCCESS");
        return Result.success();
    }
}
