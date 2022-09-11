package by.prus;

import java.util.*;
import java.util.stream.Collectors;

public class PokerHandComparator implements Comparator<PokerHand> {

    private static final HashMap<String, Integer> cardRating = new HashMap<>();
    private CardUtil cardUtil;

    static {
        cardRating.put("2", 2);
        cardRating.put("3", 3);
        cardRating.put("4", 4);
        cardRating.put("5", 5);
        cardRating.put("6", 6);
        cardRating.put("7", 7);
        cardRating.put("8", 8);
        cardRating.put("9", 9);
        cardRating.put("T", 10);
        cardRating.put("J", 11);
        cardRating.put("Q", 12);
        cardRating.put("K", 13);
        cardRating.put("A", 14);
    }

    public PokerHandComparator(CardUtil cardUtil) {
        this.cardUtil = cardUtil;
    }

    /**
     * Метод определяет приоритет набора карт, которыми владеют игроки.
     * Для того чтобы определить приоритет комбинации рассчитывается количество "очков",
     * которые набрала та или иная комбинация. Так, например, 2S 2H AD KC QC
     * имеет одну пару двоек - то есть ее главный результирующий коэффицент
     * (2+2)*CombinationFactor.PAIR = (2+2)*20=80.
     * В случае, если у другого игрока окажется такой же результат (пара двоек) с кобинацией
     * 2D 2C AS 5S 4H, то на такой случай в классе PokerHandComparator реализована логика
     * по сравнению следующих карт наивысшего номинала.
     * И так мы имеем |(2S 2H) AD KC QC| и |(2D 2C) AS 5S 4H|
     * каждая из комбинаций набрала по 80 очков. Далее вступает в силу сравнение по старшей карте
     * у комбинаций это AD и AS соответственно, они так же равны. К текущему результату кобинаций
     * прибавляется по 14 (значение A в переменной cardRating класса PokerHandComparator)
     * тогда для сравнения принимаются следующие старшие карты, KC и 5S соответственно
     * итого 80+14+13 > 80+14+5. Выигрышной оказывается комбинация 2S 2H AD KC QC
     *
     * @return результат сравнения очков первой и второй поступившей комбинации
     * @see CombinationFactor
     */
    @Override
    public int compare(PokerHand p1, PokerHand p2) {
        try {
            List<Card> cardList1 = cardUtil.getCardList(p1);
            List<Card> cardList2 = cardUtil.getCardList(p2);

            Map<Integer, List<Card>> combination_1_Rait = getCombinationRating(cardList1);
            Map<Integer, List<Card>> combination_2_Rait = getCombinationRating(cardList2);

            int rait_p1 = getRaitFromMap(combination_1_Rait);
            int rait_p2 = getRaitFromMap(combination_2_Rait);
            List<Card> cardCombination_1 = getCombinationFromMap(combination_1_Rait);
            List<Card> cardCombination_2 = getCombinationFromMap(combination_2_Rait);

            if (cardCombination_1 != null && cardCombination_2 != null) {
                cardList1.removeAll(cardCombination_1);
                cardList2.removeAll(cardCombination_2);
            }

            while (
                    rait_p1 == rait_p2 &&
                    cardList1.size() > 0 &&
                    cardList2.size() > 0
            ) {
                Card highestCard1 = getHighestCard(cardList1);
                Card highestCard2 = getHighestCard(cardList2);
                rait_p1 += cardRating.get(highestCard1.getNumber());
                rait_p2 += cardRating.get(highestCard2.getNumber());
                cardList1.remove(highestCard1);
                cardList2.remove(highestCard2);
            }
            return rait_p2 - rait_p1;
        } catch (CardDataException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int getRaitFromMap(Map<Integer, List<Card>> combination) {
        for (Map.Entry<Integer, List<Card>> map : combination.entrySet()) {
            return map.getKey();
        }
        return 0;
    }

    private List<Card> getCombinationFromMap(Map<Integer, List<Card>> combination) {
        for (Map.Entry<Integer, List<Card>> map : combination.entrySet()) {
            return map.getValue();
        }
        return null;
    }


    /**
     * Метод возвращает рейтинг по комбинации карт и список карт, задействованных в комбинации
     *
     * @param cardList - набор карт на руках у игрока на старте игры
     * @return - HashMap, которая в качестве ключа имеет рейтинг кобинации карт,
     * а значения - список карт, составивших определенную кобинацию по CombinationFactor
     * @see CombinationFactor
     */

    private Map<Integer, List<Card>> getCombinationRating(List<Card> cardList) {
        if (isStraight(cardList) && isFlash(cardList)) {
            int rait = countSumCombination(cardList) * CombinationFactor.STRAIGHT_FLUSH.getFacor();
            return new HashMap<Integer, List<Card>>() {{ put(rait, cardList); }};
        } else if (isFour(cardList)) {
            List<Card> fourCardList = getListOfTheSameNumberedCards(cardList, 4);
            int rait = countSumCombination(fourCardList) * CombinationFactor.FOUR.getFacor();
            return new HashMap<Integer, List<Card>>() {{ put(rait, fourCardList); }};
        } else if (isFulHouse(cardList)) {
            int rait = countSumCombination(cardList) * CombinationFactor.FULL_HOUSE.getFacor();
            return new HashMap<Integer, List<Card>>() {{ put(rait, cardList); }};
        } else if (isFlash(cardList)) {
            int rait = countSumCombination(cardList) * CombinationFactor.FLUSH.getFacor();
            return new HashMap<Integer, List<Card>>() {{ put(rait, cardList); }};
        } else if (isStraight(cardList)) {
            int rait = countSumCombination(cardList) * CombinationFactor.STRAIGHT.getFacor();
            return new HashMap<Integer, List<Card>>() {{ put(rait, cardList); }};
        } else if (isThree(cardList)) {
            List<Card> threeCardList = getListOfTheSameNumberedCards(cardList, 3);
            int rait = countSumCombination(threeCardList) * CombinationFactor.THREE.getFacor();
            return new HashMap<Integer, List<Card>>() {{ put(rait, threeCardList); }};
        } else if (isTwoPair(cardList)) {
            List<Card> cardListCopy = new ArrayList<>(cardList);
            List<Card> twoCardList = getListOfTheSameNumberedCards(cardListCopy, 2);
            cardListCopy.removeAll(twoCardList);
            twoCardList.addAll((getListOfTheSameNumberedCards(cardListCopy, 2)));
            int rait = countSumCombination(twoCardList) * CombinationFactor.TWO_PAIR.getFacor();
            return new HashMap<Integer, List<Card>>() {{ put(rait, twoCardList); }};
        } else if (isTwo(cardList)) {
            List<Card> twoCardList = getListOfTheSameNumberedCards(cardList, 2);
            int rait = countSumCombination(twoCardList) * CombinationFactor.PAIR.getFacor();
            return new HashMap<Integer, List<Card>>() {{ put(rait, twoCardList); }};
        } else {
            Card highestCard = getHighestCard(cardList);
            int rait = cardRating.get(highestCard.getNumber());
            return new HashMap<Integer, List<Card>>() {{ put(rait, Arrays.asList(highestCard));}};
        }
    }

    private boolean isFlash(List<Card> cardList) {
        for (int i = 1; i < cardList.size(); i++) {
            if (!cardList.get(i).getSuit().equals(cardList.get(i - 1).getSuit())) {
                return false;
            }
        }
        return true;
    }

    private boolean isStraight(List<Card> cardList) {
        List<Card> sortedList = cardList.stream().sorted(Comparator.comparing(card -> cardRating.get(card.getNumber()))).collect(Collectors.toList());
        for (int i = 1; i < sortedList.size(); i++) {
            int currentCardRating = cardRating.get(sortedList.get(i).getNumber());
            int previousCardRating = cardRating.get(sortedList.get(i-1).getNumber());
            if (currentCardRating - previousCardRating != 1) {
                return false;
            }
        }
        return true;
    }

    private boolean isFour(List<Card> cardList) {
        for (Card card : cardList) {
            if (cardList.stream().filter(c -> c.getNumber().equalsIgnoreCase(card.getNumber())).count() == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean isFulHouse(List<Card> cardList) {
        List<Card> copyList = new ArrayList<>(cardList);
        if (isThree(copyList)) {
            List<Card> threList = getListOfTheSameNumberedCards(copyList, 3);
            if (threList != null) {
                copyList.removeAll(threList);
            }
            return isTwo(copyList);
        }
        return false;
    }

    private List<Card> getListOfTheSameNumberedCards(List<Card> cardList, int cardsCount) {
        for (Card card : cardList) {
            if (cardList.stream().filter(c -> c.getNumber().equalsIgnoreCase(card.getNumber())).count() == cardsCount) {
                return cardList.stream().filter(fourcard -> fourcard.getNumber().equalsIgnoreCase(card.getNumber())).collect(Collectors.toList());
            }
        }
        return null;
    }

    private boolean isThree(List<Card> cardList) {
        for (Card card : cardList) {
            if (cardList.stream().filter(c -> c.getNumber().equalsIgnoreCase(card.getNumber())).count() == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean isTwoPair(List<Card> cardList) {
        List<Card> copyList = new ArrayList<>(cardList);
        if (isTwo(copyList)) {
            List<Card> twoList = getListOfTheSameNumberedCards(copyList, 2);
            if (twoList != null) {
                copyList.removeAll(twoList);
            }
            return isTwo(copyList);
        }
        return false;
    }

    private boolean isTwo(List<Card> cardList) {
        for (Card card : cardList) {
            if (cardList.stream().filter(c -> c.getNumber().equalsIgnoreCase(card.getNumber())).count() == 2) {
                return true;
            }
        }
        return false;
    }

    private int countSumCombination(List<Card> cardList) {
        int rating = 0;
        if (cardList != null && !cardList.isEmpty()) {
            rating = cardList.stream().mapToInt(c -> cardRating.get(c.getNumber())).sum();
        }
        return rating;
    }

    private Card getHighestCard(List<Card> cardList) {
        int cardRait = 0;
        Card returnCard = null;
        for (Card card : cardList) {
            int newRait = cardRating.get(card.getNumber());
            if (newRait > cardRait) {
                cardRait = newRait;
                returnCard = card;
            }
        }
        return returnCard;
    }

    private int getHitghestCardRating(List<Card> excludingList, List<Card> cardList) {
        int resultRating = 0;
        int positionOfBigestCard = -1;
        for (int i = 0; i < cardList.size(); i++) {
            Card currentCard = cardList.get(i);
            int currentRatingCard = cardRating.get(currentCard.getNumber());
            if (currentRatingCard > resultRating) {
                resultRating = currentRatingCard;
                positionOfBigestCard = i;
            }
        }
        if (positionOfBigestCard >= 0) {
            excludingList.add(cardList.get(positionOfBigestCard));
            cardList.remove(positionOfBigestCard);
        }
        return resultRating;
    }
}
