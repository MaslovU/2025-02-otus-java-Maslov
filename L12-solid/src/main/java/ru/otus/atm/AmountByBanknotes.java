package ru.otus.atm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AmountByBanknotes {

    private int one;
    private int five;
    private int ten;

    @Override
    public String toString() {
        return "oneRubs is: " + one +
                ", fiveRubs is: " + five +
                ", tenRubs is: " + ten;
    }
}
