package by.prus;

import java.util.Comparator;
import java.util.Objects;

public class Card {

    private String number;
    private String suit;

    public Card(String number, String suit) {
        this.number = number;
        this.suit = suit;
    }

    public String getSuit() {
        return suit.toUpperCase();
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getNumber() { return number.toUpperCase(); }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(suit.toUpperCase(), card.suit.toUpperCase()) &&
                Objects.equals(number.toUpperCase(), card.number.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, number);
    }

    @Override
    public String toString() {
        return number.toUpperCase() + suit.toUpperCase();
    }

}
