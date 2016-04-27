package syspaym.contracts.limits;

import syspaym.contracts.Service;
import syspaym.utils.DateHelper;
import syspaym.utils.Stat;
import syspaym.utils.Time;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 26.04.2016.
 */
public abstract class Limit
{
    protected String _description;
    protected Float _maxSumOfPayment;
    protected Integer _maxNumberOfPayment;
    protected Time _beginTime;
    protected Time _endTime;
    protected Long _interval;
    protected Date _lastResetStat;
    protected Map<Long, Stat> _stat;

    public Limit(){
        _lastResetStat = DateHelper.getTime();
        _stat = new HashMap<Long, Stat>();
    }

    public Limit(String description)
    {
        this();

        _description = description;
    }

    public Limit(  String description
                 , Float maxSumOfPayment) {
        this(description);

        _maxSumOfPayment = maxSumOfPayment;
    }

    public Limit(  String description
                 , Float maxSumOfPayment, Integer maxNumberOfPayment) {

        this(description, maxSumOfPayment);

        _maxNumberOfPayment = maxNumberOfPayment;
    }

    public Limit(  String description
                 , Float maxSumOfPayment
                 , Time beginTime, Time endTime) {
        this(description, maxSumOfPayment);

        _beginTime = beginTime;
        _endTime = endTime;
    }

    public Limit(  String description
                 , Float maxSumOfPayment, Integer maxNumberOfPayment
                 , Time beginTime, Time endTime) {
        this(description, maxSumOfPayment, maxNumberOfPayment);

        _beginTime = beginTime;
        _endTime = endTime;
    }

    public Limit(  String description
                 , Float maxSumOfPayment, Integer maxNumberOfPayment
                 , Time beginTime, Time endTime
                 , Long interval) {
        this(description, maxSumOfPayment, maxNumberOfPayment, beginTime, endTime);

        _interval = interval;
    }

    public abstract String getDescription();
    public abstract Float getMaxSum();
    public abstract Integer getMaxNumber();
    public abstract Date getBeginTime();
    public abstract Date getEndTime();
    public abstract Long getInterval();
    public abstract Date getLastResetStat();
    public abstract void resetStat();


   // public static Limit CreateLimit()
}
