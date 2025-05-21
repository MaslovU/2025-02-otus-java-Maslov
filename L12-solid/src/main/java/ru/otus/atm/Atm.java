package ru.otus.atm;

import ru.otus.exception.BigRequestSumException;

import java.util.List;

public interface Atm {

    void putAmount(List<AmountByBanknotes> banknotes);
    List<AmountByBanknotes> getRequiredSum(int reqSum) throws BigRequestSumException;
    int getTotal();
}
