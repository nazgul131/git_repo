package syspaym.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 26.04.2016.
 */
public class DateHelper {

    public static Date getTime()
    {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static String getTime(String format)
    {
        return format(getTime(), format);
    }

    /*
    * Обрезает время
    */
    public static Date trunc(Date datetime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(datetime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /*
     *   @format = например "dd.MM.yyyy HH:mm:ss"
     */
    public static String format(Date date, String format)
    {
        DateFormat df = new SimpleDateFormat(format);
        return  df.format(date);
    }
}
