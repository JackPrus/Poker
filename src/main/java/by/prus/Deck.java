package by.prus;

import java.util.*;

public class Deck {

    private List<Card> cardsDeck;

    public Deck() {
        cardsDeck = new LinkedList<>();
        fillCardDeck();
        Collections.shuffle(cardsDeck);
    }

    public Optional<Card> getCardFromDeck() {
        Card card = null;
        if (!cardsDeck.isEmpty()) {
            card = cardsDeck.get(0);
            cardsDeck.remove(0);
        }
        return Optional.ofNullable(card);
    }

    private void fillCardDeck() {
        for (String suit : CardUtil.getSuits()) {
            for (String number : CardUtil.getCardNumbers()) {
                cardsDeck.add(new Card(number, suit));
            }
        }
    }

    public int deckSize() { return cardsDeck.size(); }
}
