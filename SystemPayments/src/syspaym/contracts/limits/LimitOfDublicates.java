package syspaym.contracts.limits;

import syspaym.contracts.Payment;
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
public final class LimitOfDublicates extends Limit implements ILimit
{
    private Map<String, Stat> _stat;

    public LimitOfDublicates(    String description
            , Double maxSumOfPayments
            , Time beginTime, Time endTime
            , Service service)
    {
        super(description, maxSumOfPayments, beginTime, endTime);

        _service = service;
        _stat = new HashMap<String, Stat>();
    }

    public LimitOfDublicates(    String description
            , Double maxSumOfPayments
            , Long interval
            , Service service)
    {
        super(description, maxSumOfPayments, interval);

        _service = service;
        _stat = new HashMap<String, Stat>();
    }

    public LimitOfDublicates(    String description
            , Double maxSumOfPayments
            , Time beginTime, Time endTime
            , Long interval
            , Service service)
    {
        super(description, maxSumOfPayments, beginTime, endTime);

        _interval = interval;
        _service = service;
        _stat = new HashMap<String, Stat>();
    }

    @Override
    public boolean checkPayment(Payment payment) {
        if (_service == null || _service == payment.Service) {
            Date currentTime = DateHelper.getTime();
            boolean isDateIncludedToInterval = Time.checkDateIncludedToInterval(currentTime, _beginTime, _endTime);

            // если уже следующий день, нужно сбросить статистику
            if(_lastResetStat.compareTo(currentTime) < 0 && !isDateIncludedToInterval)
                resetStat();

            if (isDateIncludedToInterval &&
                    (getInterval().compareTo(0L) == 0 || (currentTime.getTime() - _lastResetStat.getTime()) <= getInterval())) {
                String key;
                if (_service == null) {
                    key = payment.Service.Id.toString() + payment.Account.Owner.Id.toString();
                } else key = _service.Id.toString() + payment.Account.Owner.Id.toString();

                if (key != null) {
                    Stat stat;
                    if (!_stat.containsKey(key)) {
                        stat = new Stat();
                        _stat.put(key, stat);
                    } else stat = _stat.get(key);

                    if (stat != null) {
                        stat.SumOfPayments += payment.Sum;
                        stat.NumberOfPayments++;

                        if(stat.NumberOfPayments > 1) {
                            // ограничение по сумме
                            if (payment.Sum > getMaxSum())
                                return false;

                            // ограничение по количеству платежей
                            if (stat.NumberOfPayments > getMaxNumber())
                                return false;
                        }
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
    public Double getMaxSum() {
        return _maxSumOfPayment;
    }

    @Override
    public Integer getMaxNumber() {
        return _maxNumberOfPayment;
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
        return (_interval == null) ? 0L : _interval;
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
