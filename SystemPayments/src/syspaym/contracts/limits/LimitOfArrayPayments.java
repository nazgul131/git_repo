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
public final class LimitOfArrayPayments extends Limit implements ILimit
{
    private Service _service;
    private Map<String, Stat> _stat;
    private boolean _isPaymentsToOneClient;

    public LimitOfArrayPayments(String description
            , Double maxSumOfPayments, Integer maxNumberOfPayments
            , Time beginTime, Time endTime
            , Service service
            , boolean isPaymentsToOneClient)
    {
        super(description, maxSumOfPayments, maxNumberOfPayments, beginTime, endTime);

        _service = service;
        _stat = new HashMap<String, Stat>();
        _isPaymentsToOneClient = isPaymentsToOneClient;
    }

    public LimitOfArrayPayments(String description
            , Double maxSumOfPayments, Integer maxNumberOfPayments
            , Long interval
            , Service service
            , boolean isPaymentsToOneClient)
    {
        super(description, maxSumOfPayments, maxNumberOfPayments, interval);

        _service = service;
        _stat = new HashMap<String, Stat>();
        _isPaymentsToOneClient = isPaymentsToOneClient;
    }

    @Override
    public boolean checkPayment(Payment payment) {
        if (_service == null || _service == payment.Service) {
            Date currentTime = DateHelper.getTime();
            boolean isDateIncludedToInterval = Time.checkDateIncludedToInterval(currentTime, _beginTime, _endTime);

            // если уже следующий день, нужно сбросить статистику
            if(_lastResetStat.compareTo(currentTime) < 0 && !isDateIncludedToInterval)
                resetStat();

            if (isDateIncludedToInterval || ((currentTime.getTime() - _lastResetStat.getTime()) <= getInterval())) {
                String key;
                if(_isPaymentsToOneClient){
                    key = payment.Account.Owner.Id.toString(); // в пользу одного клиента
                } else {
                    if (_service == null) {
                        key = "all"; // по всем услугам
                    } else key = _service.Id.toString(); // по конкретной услуге
                }

                if (key != null) {
                    Stat stat;
                    if (!_stat.containsKey(key)) {
                        stat = new Stat();
                        _stat.put(key, stat);
                    } else stat = _stat.get(key);

                    if (stat != null) {
                        stat.SumOfPayments += payment.Sum;
                        stat.NumberOfPayments++;

                        // ограничение по совокупности сумм
                        if (stat.SumOfPayments > getMaxSum())
                            return false;

                        // ограничение по количеству платежей
                        if (stat.NumberOfPayments > getMaxNumber())
                            return false;
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
