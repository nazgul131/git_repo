package syspaym.contracts.limits;

import syspaym.contracts.Payment;
import syspaym.contracts.Service;
import syspaym.utils.DateHelper;
import syspaym.utils.Stat;
import syspaym.utils.Time;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 26.04.2016.
 */
public class LimitOfService extends Limit implements ILimit
{
    private Service _service;

    public LimitOfService(    String description
                            , Float maxSumOfPayment
                            , Time beginTime, Time endTime
                            , Service service)
    {
        super(description, maxSumOfPayment, beginTime, endTime);

        _service = service;
    }

    public LimitOfService(    String description
                            , Float maxSumOfPayment
                            , Long interval
                            , Service service)
    {
        super(description, maxSumOfPayment, null, null, null, interval);

        _service = service;
    }

    @Override
    public boolean checkPayment(Payment payment) {
        if (_service == null || _service == payment.Service) {
            if (getInterval() == 0) {
                if (payment.DateTime.compareTo(getBeginTime()) >= 0 & payment.DateTime.compareTo(getEndTime()) <= 0) {
                    if (payment.Sum > getMaxSum())
                        return false;
                }
            } else {
                if ((new Date().getTime() - _lastResetStat.getTime()) > getInterval()) {
                    resetStat();
                } else{
                    Long key;
                    if(_service == null) {
                        key = payment.Service.Id;
                    } else key = _service.Id;

                    if(key != null) {
                        Stat stat;
                        if (!_stat.containsKey(key)) {
                            stat = new Stat();

                            _stat.put(key, stat);
                        } else stat = _stat.get(key);

                        // ???
                        if(stat != null) {
                            if ((stat.SumOfPayments + payment.Sum) > getMaxSum())
                                return false;
                        }

                        stat.SumOfPayments = payment.Sum;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public String getDescription() {
        return _description;
    }

    @Override
    public Float getMaxSum() {
        return _maxSumOfPayment;
    }

    @Override
    public Integer getMaxNumber() {
        return 1;
    }

    @Override
    public Date getBeginTime() {
        return _beginTime.toDate();
    }

    @Override
    public Date getEndTime() {
        return _endTime.toDate();
    }

    @Override
    public Long getInterval() {
        return _interval;
    }

    @Override
    public Date getLastResetStat() {
        return _lastResetStat;
    }

    @Override
    public void resetStat() {
        _lastResetStat = DateHelper.getTime();
        _stat.clear();
    }
}
