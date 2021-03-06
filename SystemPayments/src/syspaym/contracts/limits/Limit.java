package syspaym.contracts.limits;

import syspaym.contracts.Service;
import syspaym.utils.DateHelper;
import syspaym.utils.Sequence;
import syspaym.utils.Time;
import java.util.Date;

/**
 * Created by Admin on 26.04.2016.
 */
public abstract class Limit
{
    public Long Id;

    protected String _description;
    protected Time _beginTime;
    protected Time _endTime;
    protected Long _interval;
    protected Double _maxSumOfPayment;
    protected Integer _maxNumberOfPayment;
    protected Service _service;

    protected Date _lastResetStat;

    public Limit(){
        Id = Sequence.getNextId();

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

    public Long getServiceId(){
        if(_service != null)
            return _service.Id;

        return -1L;
    }

    public String getServiceName(){
        if(_service != null)
            return _service.Name;

        return null;
    }

    public abstract String getDescription();
    public abstract Date getBeginTime();
    public abstract Date getEndTime();
    public abstract Long getInterval();
    public abstract Double getMaxSum();
    public abstract Integer getMaxNumber();

    public abstract Date getLastResetStat();
    public abstract void resetStat();
}
