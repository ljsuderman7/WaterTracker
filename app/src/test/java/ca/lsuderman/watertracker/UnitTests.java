package ca.lsuderman.watertracker;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {
    @Test
    public void testEverydayWorkerInitialDelay() throws Exception{
        int cupResetHour = 2;
        final long MILLIS_IN_A_DAY = 86400000;

        // Calendar instances for both the current time and desired time for button/cup reset (2AM)
        Calendar desiredTime = Calendar.getInstance();
        desiredTime.set(Calendar.HOUR_OF_DAY, cupResetHour);
        desiredTime.set(Calendar.MINUTE, 0);
        desiredTime.set(Calendar.SECOND, 0);
        Calendar currentTime = Calendar.getInstance();

        // Calculates the amount of time (milliseconds) until 2AM from current time
        long delay = MILLIS_IN_A_DAY - (currentTime.getTimeInMillis() - desiredTime.getTimeInMillis());

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(EverydayWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build();

        //WorkManager workManager = WorkManager.getInstance();
        //TestDriver testDriver = WorkManagerTestInitHelper.getTestDriver();

        WorkManager.getInstance().enqueue(request).getResult().get();
        WorkInfo workInfo = WorkManager.getInstance().getWorkInfoById(request.getId()).get();

        assertEquals(workInfo.getState(), WorkInfo.State.SUCCEEDED);
    }
}