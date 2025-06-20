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
    private final Map<Integer, Integer> nominalMap = new HashMap<>();

    public void putAmount(Banknote one, Banknote five, Banknote ten) {
        nominalMap.put(1, one.getAmountByBanknotes());
        nominalMap.put(5, five.getAmountByBanknotes());
        nominalMap.put(10, ten.getAmountByBanknotes());

        totalAmount = nominalMap.get(1) * one.getNominal()
                + nominalMap.get(5) * five.getNominal()
                + nominalMap.get(10) * ten.getNominal();
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
        numbersOfBanknotesMap.put("numbersOfTenBanknotes", getBanknotes(numberTenBanknotes, nominalMap.get(10)));
        var numbersOfTenBanknotes = numbersOfBanknotesMap.get("numbersOfTenBanknotes");
        if (numbersOfTenBanknotes.equals(nominalMap.get(10))) {
            nominalMap.put(10, 0);
        }
        reqSumForClient = reqSumForClient - numbersOfTenBanknotes * ten.getNominal();

        var numberFiveBanknotesForCache = reqSumForClient / five.getNominal();
        numbersOfBanknotesMap.put("numbersOfFiveBanknotes", getBanknotes(numberFiveBanknotesForCache, nominalMap.get(5)));
        var numbersOfFiveBanknotes = numbersOfBanknotesMap.get("numbersOfFiveBanknotes");
        if (numbersOfFiveBanknotes.equals(nominalMap.get(5))) {
            nominalMap.put(5, 0);
        }
        reqSumForClient = reqSumForClient - numbersOfFiveBanknotes * five.getNominal();

        var numberOneBanknotesForCache = reqSumForClient / one.getNominal();
        if (numberOneBanknotesForCache > nominalMap.get(1)) {
            throw new BigRequestSumException("Req sum is veri big. There are not enough banknotes. Atm has ten: "
                    + nominalMap.get(10) + ", five: " + nominalMap.get(5) + ", one: " + nominalMap.get(1));
        } else {
            numbersOfBanknotesMap.put("numbersOfOneBanknotes", numberOneBanknotesForCache);
            numbersOfOneBanknotes = numbersOfBanknotesMap.get("numbersOfOneBanknotes");
            nominalMap.put(1, nominalMap.get(1) - numbersOfOneBanknotes);
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
