package syspaym.contracts;

import syspaym.utils.Sequence;

/**
 * Created by Admin on 24.04.2016.
 */
public class Account
{
    public Long Id;
    public Client Owner;
    public String Name;
    public String Number;
    private Double _rest;

    public Account(String number, Client owner)
    {
        Id = Sequence.getNextId();
        Name = "Текущий счет "+owner.Name;
        Number = number;
        Owner = owner;
        _rest = 0D;
    }

    public Double getRest()
    {
        return _rest;
    }

    public void kt(Double sum)
    {
        _rest += sum;
    }

    public void dt(Double sum)
    {
        _rest -= sum;
    }
}
