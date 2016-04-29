package syspaym.contracts;

import java.util.Date;

/**
 * Created by Admin on 24.04.2016.
 */
public class Payment
{
    public Long Id;
    public Date DateTime;
    public Account Account;
    public Service Service;
    public Double Sum;
    public EStatesPayment State;
    public String Description;
}
