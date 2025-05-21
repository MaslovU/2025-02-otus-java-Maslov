package ru.otus.atm;

public class Banknote {

    private Nominal nominal;

    public Banknote(Nominal nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal.getValue();
    }

    public void setNominal(Nominal nominal) {
        this.nominal = nominal;
    }
}
