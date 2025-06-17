package ru.otus.atm;

import ru.otus.exception.BigRequestSumException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;
import static ru.otus.atm.Nominal.*;

public class AtmRub implements Atm{

    private int totalAmount;
    private int numberOneBanknotesInAtm;
    private int numberFiveBanknotesInAtm;
    private int numberTenBanknotesInAtm;

    public void putAmount(Banknote one, Banknote five, Banknote ten) {
        numberOneBanknotesInAtm = one.getAmountByBanknotes();
        numberFiveBanknotesInAtm = five.getAmountByBanknotes();
        numberTenBanknotesInAtm = ten.getAmountByBanknotes();

        totalAmount = numberOneBanknotesInAtm * one.getNominal()
                + numberFiveBanknotesInAtm * five.getNominal()
                + numberTenBanknotesInAtm * ten.getNominal();
    }

    public List<Banknote> getRequiredSum(int reqSum, Banknote one, Banknote five, Banknote ten) throws BigRequestSumException {
        Map<String, Integer> numbersOfBanknotesMap = new HashMap<>();
        int numbersOfOneBanknotes;
        var reqSumForClient = reqSum;
        List<Banknote> banknotesListForCache = new LinkedList<>();

        if (reqSumForClient > totalAmount) {
            throw new BigRequestSumException("Req sum is veri big. Try get less sum");
        }

        var numberTenBanknotes = reqSumForClient / ten.getNominal();
        numbersOfBanknotesMap.put("numbersOfTenBanknotes", getBanknotes(numberTenBanknotes, numberOneBanknotesInAtm));
        var numbersOfTenBanknotes = numbersOfBanknotesMap.get("numbersOfTenBanknotes");
        if (numbersOfTenBanknotes == numberTenBanknotesInAtm) {
            numberTenBanknotesInAtm = 0;
        }
        reqSumForClient = reqSumForClient - numbersOfTenBanknotes * ten.getNominal();

        var numberFiveBanknotesForCache = reqSumForClient / five.getNominal();
        numbersOfBanknotesMap.put("numbersOfFiveBanknotes", getBanknotes(numberFiveBanknotesForCache, numberFiveBanknotesInAtm));
        var numbersOfFiveBanknotes = numbersOfBanknotesMap.get("numbersOfFiveBanknotes");
        if (numbersOfFiveBanknotes == numberFiveBanknotesInAtm) {
            numberFiveBanknotesInAtm = 0;
        }
        reqSumForClient = reqSumForClient - numbersOfFiveBanknotes * five.getNominal();

        var numberOneBanknotesForCache = reqSumForClient / one.getNominal();
        if (numberOneBanknotesForCache > numberOneBanknotesInAtm) {
            throw new BigRequestSumException("Req sum is veri big. There are not enough banknotes. Atm has ten: "
                    + numberTenBanknotesInAtm + ", five: " + numberFiveBanknotesInAtm + ", one: " + numberOneBanknotesInAtm);
        } else {
            numbersOfBanknotesMap.put("numbersOfOneBanknotes", numberOneBanknotesForCache);
            numbersOfOneBanknotes = numbersOfBanknotesMap.get("numbersOfOneBanknotes");
            numberOneBanknotesInAtm = numberOneBanknotesInAtm - numbersOfOneBanknotes;
            totalAmount = totalAmount - reqSum;
        }

        banknotesListForCache.add(Banknote.builder().nominal(ONE).amountByBanknotes(numbersOfOneBanknotes).build());
        banknotesListForCache.add(Banknote.builder().nominal(FIVE).amountByBanknotes(numbersOfFiveBanknotes).build());
        banknotesListForCache.add(Banknote.builder().nominal(TEN).amountByBanknotes(numbersOfTenBanknotes).build());

        return banknotesListForCache;
    }

    public int getTotal() {
        return totalAmount;
    }

    private int getBanknotes(int numberBanknotes, int numberBanknotesInAtm) {
        return min(numberBanknotes, numberBanknotesInAtm);
    }
}
