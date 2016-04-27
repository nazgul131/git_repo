package syspaym.contracts.limits;

import syspaym.contracts.Payment;

/**
 * Created by Admin on 24.04.2016.
 */
public interface ILimit
{
    boolean checkPayment(Payment payment);
    String getDescription();
}
