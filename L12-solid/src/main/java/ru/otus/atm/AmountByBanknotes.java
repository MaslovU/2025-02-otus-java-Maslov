package ru.otus.atm;

import lombok.Builder;

@Builder
public class AmountByBanknotes {

    private int one;
    private int five;
    private int ten;

    public int getOne() {
        return one;
    }

    public void setOne(int one) {
        this.one = one;
    }

    public int getFive() {
        return five;
    }

    public void setFive(int five) {
        this.five = five;
    }

    public int getTen() {
        return ten;
    }

    public void setTen(int ten) {
        this.ten = ten;
    }

    @Override
    public String toString() {
        return "oneRubs is: " + one +
                ", fiveRubs is: " + five +
                ", tenRubs is: " + ten;
    }
}
