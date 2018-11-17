package com.example.memerun.classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class recent {

    private String date;
    private double metres_run;

    public recent(double metres_run_)
    {
        Long tsLong = System.currentTimeMillis()/1000;
        date = getDateCurrentTimeZone(tsLong);
        metres_run = metres_run_;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setMetres_run(double metres_run) {
        this.metres_run = metres_run;
    }
    public double getMetres_run() {
        return metres_run;
    }

    //Fra stackOverflow

    public String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("Denmark");
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentTimeZone = (Date) calendar.getTime();
            return sdf.format(currentTimeZone);
        }catch (Exception e) {
        }
        return "";
    }

}
