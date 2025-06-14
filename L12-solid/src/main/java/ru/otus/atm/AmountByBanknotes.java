package ru.otus.atm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AmountByBanknotes {

    private int record;

    @Override
    public String toString() {
        return "amount is: " + record;
    }
}
