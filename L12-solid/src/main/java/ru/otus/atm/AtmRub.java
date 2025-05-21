package ru.otus.atm;

import ru.otus.exception.BigRequestSumException;

import static java.lang.Math.min;
import static ru.otus.atm.Nominal.*;

public class AtmRub implements Atm{

    private int totalAmount;
    private int numberOneBanknotesInAtm;
    private int numberFiveBanknotesInAtm;
    private int numberTenBanknotesInAtm;

    Banknote one = new Banknote(ONE);
    Banknote five = new Banknote(FIVE);
    Banknote ten = new Banknote(TEN);

    public void putAmount(AmountByBanknotes banknotes) {
        numberOneBanknotesInAtm = banknotes.getOne();
        numberFiveBanknotesInAtm = banknotes.getFive();
        numberTenBanknotesInAtm = banknotes.getTen();

        totalAmount = numberOneBanknotesInAtm * one.getNominal()
                + numberFiveBanknotesInAtm * five.getNominal()
                + numberTenBanknotesInAtm * ten.getNominal();
    }

    public AmountByBanknotes getRequiredSum(int reqSum) throws BigRequestSumException {
        var tenBanknotes = 0;
        var fiveBanknotes = 0;
        var oneBanknotes = 0;
        var reqSumForClient = reqSum;

        if (reqSumForClient > totalAmount) {
            throw new BigRequestSumException("Req sum is veri big. Try get less sum");
        }

        var numberTenBanknotes = reqSumForClient / ten.getNominal();
        tenBanknotes = getBanknotes(numberTenBanknotes, numberOneBanknotesInAtm);
        if (tenBanknotes == numberTenBanknotesInAtm) {
            numberTenBanknotesInAtm = 0;
        }
        reqSumForClient = reqSumForClient - tenBanknotes * ten.getNominal();

        var numberFiveBanknotesForCache = reqSumForClient / five.getNominal();
        fiveBanknotes = getBanknotes(numberFiveBanknotesForCache, numberFiveBanknotesInAtm);
        if (fiveBanknotes == numberFiveBanknotesInAtm) {
            numberFiveBanknotesInAtm = 0;
        }
        reqSumForClient = reqSumForClient - fiveBanknotes * five.getNominal();

        var numberOneBanknotesForCache = reqSumForClient / one.getNominal();
        if (numberOneBanknotesForCache > numberOneBanknotesInAtm) {
            throw new BigRequestSumException("Req sum is veri big. There are not enough banknotes. Atm has ten: "
                    + numberTenBanknotesInAtm + ", five: " + numberFiveBanknotesInAtm + ", one: " + numberOneBanknotesInAtm);
        } else {
            oneBanknotes = numberOneBanknotesForCache;
            numberOneBanknotesInAtm = numberOneBanknotesInAtm - oneBanknotes;
            totalAmount = totalAmount - reqSum;
        }

        return AmountByBanknotes.builder().one(oneBanknotes).five(fiveBanknotes).ten(tenBanknotes).build();
    }

    public int getTotal() {
        return totalAmount;
    }

    private int getBanknotes(int numberBanknotes, int numberBanknotesInAtm) {
        return min(numberBanknotes, numberBanknotesInAtm);
    }
}
