package com.daribear.PrefyBackend.Utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Get the current time of the system
 */
public class CurrentTime {

    /**
     * Returns the current time(always in GMT).
     *
     * @return the current time in milliseconds
     */
    public static long getCurrentTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return cal.getTimeInMillis();
    }
}
