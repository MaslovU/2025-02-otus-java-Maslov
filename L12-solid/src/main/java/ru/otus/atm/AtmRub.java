package ru.otus.atm;

import ru.otus.exception.BigRequestSumException;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.min;
import static ru.otus.atm.Nominal.*;

public class AtmRub implements Atm{

    private int totalAmount;
    private int numberOneBanknotesInAtm;
    private int numberFiveBanknotesInAtm;
    private int numberTenBanknotesInAtm;


    public void putAmount(List<Banknote> banknoteList) {
        numberOneBanknotesInAtm = banknoteList.get(0).getAmountByBanknotes();
        numberFiveBanknotesInAtm = banknoteList.get(1).getAmountByBanknotes();
        numberTenBanknotesInAtm = banknoteList.get(2).getAmountByBanknotes();

        totalAmount = numberOneBanknotesInAtm * banknoteList.get(0).getNominal()
                + numberFiveBanknotesInAtm * banknoteList.get(1).getNominal()
                + numberTenBanknotesInAtm * banknoteList.get(2).getNominal();
    }

    public List<Banknote> getRequiredSum(int reqSum, List<Banknote> banknoteList) throws BigRequestSumException {
        var tenBanknotes = 0;
        var fiveBanknotes = 0;
        var oneBanknotes = 0;
        var reqSumForClient = reqSum;
        List<Banknote> banknotesListForCache = new LinkedList<>();

        if (reqSumForClient > totalAmount) {
            throw new BigRequestSumException("Req sum is veri big. Try get less sum");
        }

        var ten = banknoteList.get(2);
        var numberTenBanknotes = reqSumForClient / ten.getNominal();
        tenBanknotes = getBanknotes(numberTenBanknotes, numberOneBanknotesInAtm);
        if (tenBanknotes == numberTenBanknotesInAtm) {
            numberTenBanknotesInAtm = 0;
        }
        reqSumForClient = reqSumForClient - tenBanknotes * ten.getNominal();

        var five = banknoteList.get(1);
        var numberFiveBanknotesForCache = reqSumForClient / five.getNominal();
        fiveBanknotes = getBanknotes(numberFiveBanknotesForCache, numberFiveBanknotesInAtm);
        if (fiveBanknotes == numberFiveBanknotesInAtm) {
            numberFiveBanknotesInAtm = 0;
        }
        reqSumForClient = reqSumForClient - fiveBanknotes * five.getNominal();

        var one = banknoteList.get(0);
        var numberOneBanknotesForCache = reqSumForClient / one.getNominal();
        if (numberOneBanknotesForCache > numberOneBanknotesInAtm) {
            throw new BigRequestSumException("Req sum is veri big. There are not enough banknotes. Atm has ten: "
                    + numberTenBanknotesInAtm + ", five: " + numberFiveBanknotesInAtm + ", one: " + numberOneBanknotesInAtm);
        } else {
            oneBanknotes = numberOneBanknotesForCache;
            numberOneBanknotesInAtm = numberOneBanknotesInAtm - oneBanknotes;
            totalAmount = totalAmount - reqSum;
        }

        banknotesListForCache.add(Banknote.builder().nominal(ONE).amountByBanknotes(oneBanknotes).build());
        banknotesListForCache.add(Banknote.builder().nominal(FIVE).amountByBanknotes(fiveBanknotes).build());
        banknotesListForCache.add(Banknote.builder().nominal(TEN).amountByBanknotes(tenBanknotes).build());

        return banknotesListForCache;
    }

    public int getTotal() {
        return totalAmount;
    }

    private int getBanknotes(int numberBanknotes, int numberBanknotesInAtm) {
        return min(numberBanknotes, numberBanknotesInAtm);
    }
}
