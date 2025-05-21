package ru.otus;

import ru.otus.atm.AmountByBanknotes;
import ru.otus.atm.AtmRub;
import ru.otus.exception.BigRequestSumException;

public class Main {
    public static void main(String[] args) {
        AtmRub atm = new AtmRub();
        AmountByBanknotes sum;

        AmountByBanknotes byBanknotes = AmountByBanknotes.builder().one(1).five(3).ten(2).build();

        atm.putAmount(byBanknotes);

        System.out.println("Atm has total sum: " + atm.getTotal());

        try {
            sum = atm.getRequiredSum(23);
            System.out.println("Got banknotes: " + sum);
            int balance = atm.getTotal();
            System.out.println("Current balance is: " + balance);
        } catch (BigRequestSumException e) {
            System.out.println(e.getMessage());
        }
    }
}
