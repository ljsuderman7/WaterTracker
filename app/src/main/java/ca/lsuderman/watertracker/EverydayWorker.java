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

        Log.d("Did it work?", "WORKED!!!");
        return Result.success();
    }
}
