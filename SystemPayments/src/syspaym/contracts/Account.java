package syspaym.contracts;

/**
 * Created by Admin on 24.04.2016.
 */
public class Account
{
    public Long Id;
    public Client Owner;
    public String Number;
    public Double Rest;

    public Account(String number, Client owner)
    {
        Number = number;
        Owner = owner;
        Rest = 0D;
    }

    public void kt(Double sum)
    {
        Rest += sum;
    }

    public void dt(Double sum)
    {
        Rest -= sum;
    }
}
