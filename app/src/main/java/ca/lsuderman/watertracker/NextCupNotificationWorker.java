package ca.lsuderman.watertracker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NextCupNotificationWorker extends Worker {
    public NextCupNotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        // If the last cup is done, cancel the notifications
        Cup lastCup = new Cup();
        lastCup = ((WaterDB) getApplicationContext()).getCup(8);
        if (lastCup.getIsDone()){ //TODO: OR after Bedtime?
            WorkManager.getInstance().cancelAllWorkByTag("next_cup_notification");
        } else {
            //TODO: Check for correct amount of cups finished by predetermined time interval
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.setContext(getApplicationContext());
            notificationHelper.createNotification();
        }

        return Result.success();
    }
}
