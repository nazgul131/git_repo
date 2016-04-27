package syspaym.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayDeque;
import syspaym.contracts.Payment;

/**
 * Created by Admin on 24.04.2016.
 */
public class PaymentsServlet extends HttpServlet
{
    private ArrayDeque<Payment> _payments;

    public PaymentsServlet(ArrayDeque<Payment> queuePayments)
    {
        _payments = queuePayments;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        response.getWriter().println("HelloWorld!");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }
}
