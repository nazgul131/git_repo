package syspaym.contracts.limits;

import syspaym.contracts.Payment;
import syspaym.contracts.Service;
import syspaym.utils.DateHelper;
import syspaym.utils.Time;
import java.util.Date;

/**
 * Created by Admin on 26.04.2016.
 */
public final class LimitOfService extends Limit implements ILimit {
    private Service _service;

    public LimitOfService(String description
            , Double maxSumOfPayment
            , Time beginTime, Time endTime
            , Service service) {
        super(description, maxSumOfPayment, beginTime, endTime);

        _service = service;
    }

    public LimitOfService(String description
            , Double maxSumOfPayment
            , Long interval
            , Service service) {
        super(description, maxSumOfPayment, interval);

        _service = service;
    }

    private boolean validateSum(Payment payment) {
        return (payment.Sum <= getMaxSum());
    }

    @Override
    public boolean checkPayment(Payment payment) {
        if (_service == null || _service == payment.Service) {
            if (getInterval() == 0) {
                if (Time.checkDateIncludedToInterval(payment.DateTime, _beginTime, _endTime) || // если задан период времени, например с 9:00 до 18:00, или с 23:00 до 9:00
                        (DateHelper.getTime().getTime() - _lastResetStat.getTime()) <= getInterval()) // если задан интервал, например 1 час (= 3600000 в формате Long(в миллисекундах))
                    if (payment.Sum > getMaxSum())
                        return false;

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
    }
}
