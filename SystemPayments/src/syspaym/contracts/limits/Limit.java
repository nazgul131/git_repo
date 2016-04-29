package syspaym.contracts.limits;

import syspaym.utils.DateHelper;
import syspaym.utils.Time;
import java.util.Date;

/**
 * Created by Admin on 26.04.2016.
 */
public abstract class Limit
{
    protected String _description;
    protected Double _maxSumOfPayment;
    protected Integer _maxNumberOfPayment;
    protected Time _beginTime;
    protected Time _endTime;
    protected Long _interval;
    protected Date _lastResetStat;

    public Limit(){
        _lastResetStat = DateHelper.getTime();
    }

    public Limit(String description)
    {
        this();

        _description = description;
    }

    public Limit(  String description
                 , Double maxSumOfPayment) {
        this(description);

        _maxSumOfPayment = maxSumOfPayment;
    }

    public Limit(  String description
                 , Double maxSumOfPayment, Integer maxNumberOfPayment) {

        this(description, maxSumOfPayment);

        _maxNumberOfPayment = maxNumberOfPayment;
    }

    public Limit(  String description
                 , Double maxSumOfPayment
                 , Time beginTime, Time endTime) {
        this(description, maxSumOfPayment);

        _beginTime = beginTime;
        _endTime = endTime;
    }

    public Limit(   String description
                  , Double maxSumOfPayment
                  , Long interval) {
        this(description, maxSumOfPayment);

        _interval = interval;
    }

    public Limit(  String description
                 , Double maxSumOfPayment, Integer maxNumberOfPayment
                 , Time beginTime, Time endTime) {
        this(description, maxSumOfPayment, maxNumberOfPayment);

        _beginTime = beginTime;
        _endTime = endTime;
    }

    public Limit(  String description
                 , Double maxSumOfPayment, Integer maxNumberOfPayment
                 , Long interval) {
        this(description, maxSumOfPayment, maxNumberOfPayment);

        _interval = interval;
    }

    public abstract String getDescription();
    public abstract Double getMaxSum();
    public abstract Integer getMaxNumber();
    public abstract Date getBeginTime();
    public abstract Date getEndTime();
    public abstract Long getInterval();
    public abstract Date getLastResetStat();
    public abstract void resetStat();

   // public static Limit CreateLimit()
}
