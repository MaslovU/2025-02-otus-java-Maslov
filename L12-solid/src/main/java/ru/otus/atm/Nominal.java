package ru.otus.atm;

import lombok.Getter;

@Getter
public enum Nominal {

    ZERO(0),
    ONE(1),
    FIVE(5),
    TEN(10);

    private final int value;

    Nominal(int i) {
        value = i;
    }

}
