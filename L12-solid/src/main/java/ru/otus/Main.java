package ru.otus;

import ru.otus.atm.AmountByBanknotes;
import ru.otus.atm.AtmRub;
import ru.otus.exception.BigRequestSumException;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AtmRub atm = new AtmRub();
        List<AmountByBanknotes> sum;

        AmountByBanknotes byOneRubBanknotes = AmountByBanknotes.builder().amount(3).build();
        AmountByBanknotes byFiveRubBanknotes = AmountByBanknotes.builder().amount(2).build();
        AmountByBanknotes byTenRubBanknotes = AmountByBanknotes.builder().amount(3).build();

        List<AmountByBanknotes> list = new LinkedList<>();
        list.add(byOneRubBanknotes);
        list.add(byFiveRubBanknotes);
        list.add(byTenRubBanknotes);

        atm.putAmount(list);

        System.out.println("Atm has total sum: " + atm.getTotal());

        try {
            sum = atm.getRequiredSum(23);
            System.out.println("Got banknotes: one rub " + sum.get(0)
                    + ", five rubs " + sum.get(1)
                    + ", ten rubs " + sum.get(2));
            int balance = atm.getTotal();
            System.out.println("Current balance is: " + balance);
        } catch (BigRequestSumException e) {
            System.out.println(e.getMessage());
        }
    }
}
