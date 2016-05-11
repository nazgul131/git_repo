package syspaym.contracts.limits;

import syspaym.contracts.Payment;

import java.util.Date;

/**
 * Created by Admin on 24.04.2016.
 */
public interface ILimit
{
    boolean checkPayment(Payment payment);

    Long getServiceId();
    String getServiceName();

    String getDescription();
    Double getMaxSum();
    Integer getMaxNumber();
    Date getBeginTime();
    Date getEndTime();
    Long getInterval();
}
