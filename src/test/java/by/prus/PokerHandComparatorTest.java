package by.prus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class PokerHandComparatorTest {

    Deck deck;
    CardUtil cardUtil;
    PokerHandComparator comparator;
    List<PokerHand> players = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        deck = new Deck();
        cardUtil = new CardUtil();
        comparator = new PokerHandComparator(cardUtil);
    }

    @Test
    public void testCompare() {
        // royal flash
        PokerHand hand1 = new PokerHand("AS KS qS JS Ts");
        PokerHand hand2 = new PokerHand("2S 3S 4S 5S 6s");

        //four
        PokerHand hand6 = new PokerHand("AS AH AD AC Ts");
        PokerHand hand7 = new PokerHand("2S 2H 2D 2C 6s");

        //fullHouse
        PokerHand hand8 = new PokerHand("AS AH AD KC Ks");
        PokerHand hand9 = new PokerHand("2S 2H 2D 3C 3D");

        //flush
        PokerHand hand10 = new PokerHand("AS KS QS JS 9S");
        PokerHand hand11 = new PokerHand("2D 3D 4D 5D 7D");

        //straight
        PokerHand hand3 = new PokerHand("AS KH QS JS Ts");
        PokerHand hand4 = new PokerHand("2S 3C 4S 5S 6s");
        PokerHand hand5 = new PokerHand("9S KS qS JS Ts");

        //three
        PokerHand hand12 = new PokerHand("AS AD AC JS TS");
        PokerHand hand13 = new PokerHand("2S 2D 2C 5D 6D");

        //two
        PokerHand hand14 = new PokerHand("AS AD KC KS TS");
        PokerHand hand15 = new PokerHand("2S 2D 3C 3D 6D");
        PokerHand hand16 = new PokerHand("2H 2C 3H 3S 7D");

        //pair
        PokerHand hand17 = new PokerHand("AS AD KC QS JS");
        PokerHand hand18 = new PokerHand("2S 2D 3C 4D 5D");
        PokerHand hand19 = new PokerHand("2C 2H 3D 4S 6D");

        //one
        PokerHand hand20 = new PokerHand("AS KD QC JS 9S");
        PokerHand hand21 = new PokerHand("2S 3D 4C 5D 7D");
        PokerHand hand22 = new PokerHand("2S 3D 4C 5D 8D");

        Assert.assertTrue(comparator.compare(hand1, hand2) < 0);
        Assert.assertTrue(comparator.compare(hand6, hand7) < 0);
        Assert.assertTrue(comparator.compare(hand2, hand7) < 0);
        Assert.assertTrue(comparator.compare(hand8, hand9) < 0);
        Assert.assertTrue(comparator.compare(hand7, hand8) < 0);
        Assert.assertTrue(comparator.compare(hand10, hand11) < 0);
        Assert.assertTrue(comparator.compare(hand9, hand10) < 0);
        Assert.assertTrue(comparator.compare(hand2, hand3) < 0);
        Assert.assertTrue(comparator.compare(hand3, hand4) < 0);
        Assert.assertTrue(comparator.compare(hand5, hand4) < 0);
        Assert.assertTrue(comparator.compare(hand12, hand13) < 0);
        Assert.assertTrue(comparator.compare(hand14, hand15) < 0);
        Assert.assertTrue(comparator.compare(hand16, hand15) < 0);
        Assert.assertTrue(comparator.compare(hand13, hand14) < 0);
        Assert.assertTrue(comparator.compare(hand17, hand18) < 0);
        Assert.assertTrue(comparator.compare(hand16, hand17) < 0);
        Assert.assertTrue(comparator.compare(hand19, hand18) < 0);
        Assert.assertTrue(comparator.compare(hand19, hand20) < 0);
        Assert.assertTrue(comparator.compare(hand20, hand21) < 0);
        Assert.assertTrue(comparator.compare(hand22, hand21) < 0);

        List<PokerHand> pokerHands = new ArrayList<>();
        pokerHands.add(hand1);//0
        pokerHands.add(hand6);//1
        pokerHands.add(hand8);//2
        pokerHands.add(hand10);//3
        pokerHands.add(hand3);//4
        pokerHands.add(hand12);//5
        pokerHands.add(hand14);//6
        pokerHands.add(hand16);//7
        pokerHands.add(hand17);//8
        pokerHands.add(hand19);//9
        pokerHands.add(hand20);//10
        pokerHands.add(hand22);//11

        Collections.shuffle(pokerHands);
        Assert.assertNotEquals(pokerHands.get(0), hand1);
        pokerHands.sort(comparator);
        Assert.assertEquals(pokerHands.get(0), hand1);
        Assert.assertEquals(pokerHands.get(1), hand6);
        Assert.assertEquals(pokerHands.get(2), hand8);
        Assert.assertEquals(pokerHands.get(3), hand10);
        Assert.assertEquals(pokerHands.get(4), hand3);
        Assert.assertEquals(pokerHands.get(5), hand12);
        Assert.assertEquals(pokerHands.get(6), hand14);
        Assert.assertEquals(pokerHands.get(7), hand16);
        Assert.assertEquals(pokerHands.get(8), hand17);
        Assert.assertEquals(pokerHands.get(9), hand19);
        Assert.assertEquals(pokerHands.get(10), hand20);
        Assert.assertEquals(pokerHands.get(11), hand22);
    }

    /**
     * Метод генерирует 50 игр с рандомным числом игроков, имеющих рандомный набор карт
     * Метод провреяет не выйдет ли где-либо исключения при обработке данных и все ли нормально функционирует
     */
    @Test
    public void startGame() throws CardDataException {
        for (int i = 0; i < 50; i++) {
            deck = new Deck();
            players.clear();
            generatePlayersList(new Random().nextInt(10) + 1); //От 1 до 10 игроков
            System.out.println("_________________________________________");
            System.out.println(String.format("Игра № %d началась", i + 1));
            List<PokerHand> sortedList = players.stream().sorted(comparator).collect(Collectors.toList());
            System.out.println(String.format("Игра № %d окончена. Победившая комбинация: %s", i, sortedList.get(0).getCombination()));
            System.out.println("_________________________________________");
        }
    }

    private void generatePlayersList(int quantity) throws CardDataException {
        if (quantity > 0 && quantity < 11) {
            for (int i = 0; i < quantity; i++) {
                PokerHand pokerHand = new PokerHand(cardsToString(giveCardsForPlayer()));
                System.out.println(String.format("Игрок %d создан. Набор карт: %s", i + 1, pokerHand.getCombination()));
                players.add(pokerHand);
            }
        }
    }

    private List<Card> giveCardsForPlayer() throws CardDataException {
        List<Card> cardsOfPlayer = new ArrayList<>();
        if (deck.deckSize() > 4) {
            for (int i = 0; i < 5; i++) {
                cardsOfPlayer.add(deck.getCardFromDeck().orElseThrow(() -> new CardDataException("The Deck is empty")));
            }
        } else {
            deck = new Deck();
            for (int i = 0; i < 5; i++) {
                cardsOfPlayer.add(deck.getCardFromDeck().orElseThrow(() -> new CardDataException("The Deck is empty")));
            }
        }
        return cardsOfPlayer;
    }

    private String cardsToString(List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.getNumber().toUpperCase());
            sb.append(card.getSuit().toUpperCase());
            sb.append(" ");
        }
        return sb.toString();
    }
}