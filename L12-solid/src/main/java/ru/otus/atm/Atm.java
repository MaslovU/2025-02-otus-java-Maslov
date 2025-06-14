package ru.otus.atm;

import ru.otus.exception.BigRequestSumException;

import java.util.List;

public interface Atm {

    void putAmount(Banknote one, Banknote five, Banknote ten);
    List<Banknote> getRequiredSum(int reqSum, Banknote one, Banknote five, Banknote ten) throws BigRequestSumException;
    int getTotal();
}
