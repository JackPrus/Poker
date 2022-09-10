package by.prus;

import java.util.Objects;

public class PokerHand {

    private String combination;

    public PokerHand(String combination) {
        this.combination = combination;
    }

    public String getCombination() {
        return combination;
    }

    public void setCombination(String combination) {
        this.combination = combination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokerHand pokerHand = (PokerHand) o;
        return Objects.equals(combination, pokerHand.combination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(combination);
    }
}
