package ru.otus;

import ru.otus.atm.AtmRub;
import ru.otus.atm.Banknote;
import ru.otus.atm.Nominal;
import ru.otus.exception.BigRequestSumException;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AtmRub atm = new AtmRub();
        List<Banknote> sum;

        Banknote one = Banknote.builder().nominal(Nominal.ONE).amountByBanknotes(3).build();
        Banknote five = Banknote.builder().nominal(Nominal.FIVE).amountByBanknotes(2).build();
        Banknote ten = Banknote.builder().nominal(Nominal.TEN).amountByBanknotes(3).build();

        atm.putAmount(one, five, ten);

        System.out.println("Atm has total sum: " + atm.getTotal());

        try {
            sum = atm.getRequiredSum(26, one, five, ten);
            System.out.println("Got banknotes: one rub " + sum.get(0).getAmountByBanknotes()
                    + ", five rubs " + sum.get(1).getAmountByBanknotes()
                    + ", ten rubs " + sum.get(2).getAmountByBanknotes());
            int balance = atm.getTotal();
            System.out.println("Current balance is: " + balance);
        } catch (BigRequestSumException e) {
            System.out.println(e.getMessage());
        }
    }
}
