package ru.otus.atm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AmountByBanknotes {

    private int amount;

    @Override
    public String toString() {
        return "amount is: " + amount;
    }
}
