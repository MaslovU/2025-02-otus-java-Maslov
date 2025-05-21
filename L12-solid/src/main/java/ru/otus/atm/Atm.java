package ru.otus.atm;

import ru.otus.exception.BigRequestSumException;

public interface Atm {

    void putAmount(AmountByBanknotes banknotes);
    AmountByBanknotes getRequiredSum(int reqSum) throws BigRequestSumException;
    int getTotal();
}
