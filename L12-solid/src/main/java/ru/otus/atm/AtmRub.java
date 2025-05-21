package ru.otus.atm;

import ru.otus.exception.BigRequestSumException;

import java.util.List;

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

    public void putAmount(List<AmountByBanknotes> banknotes) {
        numberOneBanknotesInAtm = banknotes.get(0).getAmount();
        numberFiveBanknotesInAtm = banknotes.get(1).getAmount();
        numberTenBanknotesInAtm = banknotes.get(2).getAmount();

        totalAmount = numberOneBanknotesInAtm * one.getNominal()
                + numberFiveBanknotesInAtm * five.getNominal()
                + numberTenBanknotesInAtm * ten.getNominal();
    }

    public List<AmountByBanknotes> getRequiredSum(int reqSum) throws BigRequestSumException {
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

        var oneAmount = AmountByBanknotes.builder().amount(oneBanknotes).build();
        var fiveAmount = AmountByBanknotes.builder().amount(fiveBanknotes).build();
        var tenAmount = AmountByBanknotes.builder().amount(tenBanknotes).build();

        return List.of(oneAmount, fiveAmount, tenAmount);
    }

    public int getTotal() {
        return totalAmount;
    }

    private int getBanknotes(int numberBanknotes, int numberBanknotesInAtm) {
        return min(numberBanknotes, numberBanknotesInAtm);
    }
}
