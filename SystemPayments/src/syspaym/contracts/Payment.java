package syspaym.contracts;

import syspaym.utils.Sequence;

import java.util.Date;

/**
 * Created by Admin on 24.04.2016.
 */
public class Payment
{
    public class NullAccountException extends Exception{
        public NullAccountException(){
            super("Account is null!");
        }
    }
    public class NullServiceException extends Exception{
        public NullServiceException(){
            super("Service is null!");
        }
    }
    public class InvalidSumException extends Exception{
        public InvalidSumException(){
            super("Amount must be greater than zero!");
        }
    }

    public Long Id;
    public Date DateTime;
    public Account Account;
    public Service Service;
    public Double Sum;
    public EStatesPayment State;
    public String Description;

    public Payment(Date datetime, Account account, Service service, Double sum, String description) throws NullAccountException, NullServiceException, InvalidSumException {
        if(account == null)
            throw new NullAccountException();

        if(service == null)
            throw new NullServiceException();

        if(sum == null || sum <= 0)
            throw new InvalidSumException();

        Id = Sequence.getNextId();
        DateTime = datetime;
        Account = account;
        Service = service;
        Sum = sum;
        Description = description;
        State = EStatesPayment.New;
    }
}
