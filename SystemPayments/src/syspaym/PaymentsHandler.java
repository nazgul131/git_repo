package syspaym;

import syspaym.contracts.EStatesPayment;
import syspaym.contracts.limits.ILimit;
import syspaym.contracts.Payment;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by Admin on 24.04.2016.
 */
public class PaymentsHandler implements Runnable
{
    private ArrayDeque<Payment> _queuePayments;
    private ArrayList<Payment> _historyPayments;
    private ArrayList<ILimit> _limits;

    private boolean _isWork;

    public PaymentsHandler(ArrayDeque<Payment> queuePayments, ArrayList<Payment> historyPayments, ArrayList<ILimit> limits)
    {
        _queuePayments = queuePayments;
        _historyPayments = historyPayments;
        _limits = limits;

        _isWork = true;
    }

    /*
     Метод обрабатывает платежи, в соответствии с переданными лимитами.
     */
    public void startHandle() throws InterruptedException {
        _isWork = true;

        while(_isWork)
        {
            Payment payment = _queuePayments.pollFirst();
            if(payment != null) {
                handlePayment(payment);

                _historyPayments.add(payment);
            }

            Thread.sleep(300);
        }
    }

    public void stopHandle()
    {
        _isWork = false;
    }

    /*
     Метод проверяет платеж, в соответствии с лимитами.
     */
    private boolean checkPayment(Payment payment)
    {
        // пройдемся по всем лимитам...
        for (ILimit limit : _limits)
        {
            if(!limit.checkPayment(payment))
                return false;
        }

        // если платеж прошел все проверки
        return true;
    }

    /*
    Метод обрабатывает платеж.
     */
    private void handlePayment(Payment payment)
    {
        if(!checkPayment(payment))
            payment.State = EStatesPayment.Tbc;
        else
            payment.State = EStatesPayment.OK;
    }

    @Override
    public void run() {
        try {
            startHandle();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
