package ru.otus.atm;

import ru.otus.exception.BigRequestSumException;

import java.util.List;

public interface Atm {

    void putAmount(List<Banknote> banknoteList);
    List<Banknote> getRequiredSum(int reqSum, List<Banknote> banknoteList) throws BigRequestSumException;
    int getTotal();
}
