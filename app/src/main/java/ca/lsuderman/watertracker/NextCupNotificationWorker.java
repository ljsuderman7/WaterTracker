package ca.lsuderman.watertracker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

public class NextCupNotificationWorker extends Worker {
    private SharedPreferences sharedPreferences;
    public NextCupNotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int bedtime = Integer.parseInt(sharedPreferences.getString("bedtime", "23"));
        int wakeUp = Integer.parseInt(sharedPreferences.getString("wakeUp", "8"));

        Calendar currentTime = Calendar.getInstance();

        Calendar bedtimeCalendar = Calendar.getInstance();
        bedtimeCalendar.set(Calendar.HOUR_OF_DAY, bedtime);
        bedtimeCalendar.set(Calendar.MINUTE, 0);
        bedtimeCalendar.set(Calendar.SECOND, 0);

        Calendar wakeUpCalendar = Calendar.getInstance();
        wakeUpCalendar.set(Calendar.HOUR_OF_DAY, wakeUp);
        wakeUpCalendar.set(Calendar.MINUTE, 0);
        wakeUpCalendar.set(Calendar.SECOND, 0);

        if (currentTime.getTimeInMillis() >= bedtimeCalendar.getTimeInMillis()){
            WorkManager.getInstance().cancelAllWorkByTag("next_notification");
        } else if (currentTime.getTimeInMillis() >= wakeUpCalendar.getTimeInMillis()) {
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.setContext(getApplicationContext());
            notificationHelper.createNotification();
        }
        return Result.success();
    }
}
