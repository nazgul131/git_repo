package syspaym.contracts.limits;

import syspaym.contracts.Payment;
import syspaym.utils.DateHelper;
import syspaym.utils.Stat;
import syspaym.utils.Time;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 26.04.2016.
 */
public class LimitOfDublicates extends Limit implements ILimit {
    private Map<String, Stat> _stat; // ключ типа String будет содержать "<номер счета>+<логин клиента>"

    public LimitOfDublicates(    Float maxSumOfPayments, Integer maxNumberOfPayments
                               , Time beginTime, Time endTime)
    {
        _maxSumOfPayment = maxSumOfPayments;
        _maxNumberOfPayment = maxNumberOfPayments;

        _beginTime = beginTime;
        _endTime = endTime;

        _stat = new HashMap<String, Stat>();
    }


    @Override
    public boolean checkPayment(Payment payment) {
        return false;
    }

    @Override
    public Float getMaxSum() {
        return null;
    }

    @Override
    public Integer getMaxNumber() {
        return null;
    }

    @Override
    public Date getBeginTime() {
        return null;
    }

    @Override
    public Date getEndTime() {
        return null;
    }

    @Override
    public Long getInterval() {
        return 0L;
    }

    @Override
    public void resetStat() {
        _stat.clear();
        _lastResetStat = DateHelper.getTime();
    }
}
