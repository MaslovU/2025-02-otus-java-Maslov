package ru.otus.atm;

import lombok.*;

@Setter
@Builder
@AllArgsConstructor
public class Banknote {

    private Nominal nominal;
    @Getter
    private int amountByBanknotes;

    public int getNominal() {
        return nominal.getValue();
    }

}
