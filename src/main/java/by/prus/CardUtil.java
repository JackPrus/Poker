package by.prus;

import org.jcp.xml.dsig.internal.dom.ApacheOctetStreamData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class CardUtil {

    public static final String[] suits = new String[]{"S", "H", "D", "C"};
    public static final String[] cardNumbers = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};

    public static String[] getSuits() {
        return Arrays.copyOf(suits, suits.length);
    }

    public static String[] getCardNumbers() {
        return Arrays.copyOf(cardNumbers, cardNumbers.length);
    }

    public List<Card> getCardList(PokerHand pokerHand) throws CardDataException {
        if (pokerHand != null && pokerHand.getCombination() != null){
            String[] cards = pokerHand.getCombination().split("\\s");
            List<Card> resultList = createCardListFromCardsArray(cards);
            cardSetLegal(resultList);
            return resultList;
        }else {
            throw new CardDataException("The data is Null");
        }
    }

    public List<Card> createCardListFromCardsArray(String[] cards) throws CardDataException {
        List<Card> resultList = new ArrayList<>();
        if (cards.length == 5) {
            for (String card : cards) {
                char[] cardData = card.toCharArray();
                if (cardData.length == 2) {
                    resultList.add(new Card(String.valueOf(cardData[0]), String.valueOf(cardData[1])));
                } else {
                    throw new CardDataException("Illegal card data for card:" + card);
                }
            }
        } else {
            throw new CardDataException("The card set must be 5. Current card set is : " + String.join(" ", cards));
        }
        return resultList;
    }

    public boolean cardSetLegal(List<Card> cards) throws CardDataException {
        for (Card card : cards) {
            if (!cardExists(card)) {
                throw new CardDataException("The card is not exists: " + card);
            }
        }
        return true;
    }

    public boolean cardExists(Card card) {
        boolean suitExist = Arrays.stream(suits).anyMatch(card.getSuit()::equalsIgnoreCase);
        boolean numberExist = Arrays.stream(cardNumbers).anyMatch(card.getNumber()::equalsIgnoreCase);
        return suitExist && numberExist;
    }
}