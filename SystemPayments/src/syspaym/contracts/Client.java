package syspaym.contracts;

import java.util.ArrayList;

/**
 * Created by Admin on 24.04.2016.
 */
public class Client
{
    public Long Id;
    public String Name;
    public ArrayList<Account> Accounts;

    // по идее надо делать еще класс User, но для примитивной системы и так сойдет
    public String Login;
    public Integer PsswHash;
    public boolean IsAuthorized;

    public Client(String name, String login, String pssw)
    {
        Name = name;
        Login = login;
        PsswHash = pssw.hashCode();

        Accounts = new ArrayList<Account>();

        IsAuthorized = false;
    }
}
