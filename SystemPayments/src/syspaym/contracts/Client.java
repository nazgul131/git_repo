package syspaym.contracts;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Admin on 24.04.2016.
 */
public class Client
{
    public Long Id;
    public String Name;
    public ArrayList<Account> Accounts;

    // по идее надо делать еще класс User и Rights, но для примитивной системы и так сойдет
    public String Login;
    public Integer PsswHash;
    public boolean IsOnline;
    public boolean IsAdmin;

    public Client(String name, String login, String pssw)
    {
        Name = name;
        Login = login;
        PsswHash = pssw.hashCode();

        Accounts = new ArrayList<Account>();

        IsOnline = false;
        IsAdmin = false;
    }
}
