package ca.lsuderman.watertracker;

import java.util.Calendar;

public class Utilities {
    public static long getInitialDelay(int hour){
        long delay = 0;

        final long MILLIS_IN_A_DAY = 86400000;

        // Calendar instances for both the current time and desired time for button/cup reset (2AM)
        Calendar desiredTime = Calendar.getInstance();
        desiredTime.set(Calendar.HOUR_OF_DAY, hour);
        desiredTime.set(Calendar.MINUTE, 0);
        desiredTime.set(Calendar.SECOND, 0);
        Calendar currentTime = Calendar.getInstance();

        // Calculates the amount of time (milliseconds) until 2AM from current time
        delay = MILLIS_IN_A_DAY - (currentTime.getTimeInMillis() - desiredTime.getTimeInMillis());

        return delay;
    }
}
