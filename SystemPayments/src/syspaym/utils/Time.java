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
    * Метод возвращает
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

        if(cal.getTime().compareTo(new Date()) > 0)
            cal.add(Calendar.DAY_OF_YEAR, -1);

        return cal.getTime();
    }

    public Float toFloat()
    {
        return (Float)(_hours+(_minutes*0.01F));
    }

    @Override
    public String toString()
    {
        return _hours.toString()+":"+_minutes.toString();
    }
}
