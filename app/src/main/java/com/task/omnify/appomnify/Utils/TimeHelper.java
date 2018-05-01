package com.task.omnify.appomnify.Utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeHelper {
    public static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    public static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");

    public static String toDuration(long timePast) {
        long duration=(System.currentTimeMillis())-timePast*1000;

        StringBuffer res = new StringBuffer();
        for (int i = 0; i < TimeHelper.times.size(); i++) {
            Long current = TimeHelper.times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                res.append(temp).append(" ").append(TimeHelper.timesString.get(i)).append(temp != 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if ("".equals(res.toString()))
            return "0 seconds ago";
        else
            return res.toString();
    }

    public static String toPrettyDate(long time){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time*1000);
        String format = new SimpleDateFormat("E, MMM d, yyyy").format(cal.getTime());
        return format;


    }


}