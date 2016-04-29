package syspaym.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 26.04.2016.
 */
public class Time {
    private Integer _hours;
    private Integer _minutes;

    public Time(Integer hours, Integer minutes)
    {
        if(hours > 23 || hours < 0)
            _hours = 0;
        else _hours = hours;

        if(minutes > 59 || minutes < 0)
            _minutes = 0;
        else _minutes = minutes;
    }

    public Integer getHours(){return _hours;}
    public Integer getMinutes() {return _minutes;}

    /*
    * Метод возвращает разницу (время в формате Double: целая часть = часы, десятичная = минуты)
    * между этим временем и временем указанным в параметре.
    * Если 0, то времена равны, если < 0, то это время меньше узказанного в параметре и наоборот.
     */
    public Double compareTo(Time anotherTime) {
        int hours = ((_hours == 0) ? 24 : _hours) - ((anotherTime.getHours() == 0) ? 24 : anotherTime.getHours());
        long minutes = _minutes - anotherTime.getMinutes();

        double min = minutes / 60.0D;
        hours += Math.round(min);
        minutes = Math.round((min - Math.round(min)) * 60.0D);

        return (Double) (hours + (minutes * 0.01D));
    }

    public static Time timeFromHHmm(String HHmm){
        Integer hours = Integer.getInteger(HHmm.substring(0, 1));
        Integer minutes = Integer.getInteger(HHmm.substring(2,3));

        return new Time(hours, minutes);
    }

    /*
    * Возвращает текущую дату + время, взятое из полей
     */
    public Date toDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, _hours);
        cal.set(Calendar.MINUTE, _minutes);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public Double toDouble()
    {
        return (Double)(_hours + (_minutes*0.01D));
    }

    @Override
    public String toString()
    {
        return _hours.toString()+":"+_minutes.toString();
    }

    public static boolean checkDateIncludedToInterval(Date date, Time timeStart, Time timeEnd)
    {
        if(date == null || timeStart == null || timeEnd == null)
            return false;

        Double timeDiff = timeStart.compareTo(timeEnd);

        // ночь
        if(timeDiff >= 0){
            if (date.compareTo(timeStart.toDate()) <= 0 & date.compareTo(timeEnd.toDate()) >= 0)
                return true;
        }
        //день
        else /*if (timeDiff < 0)*/{
            if (date.compareTo(timeStart.toDate()) >= 0 & date.compareTo(timeEnd.toDate()) <= 0)
                return true;
        }

        return false;
    }
}
