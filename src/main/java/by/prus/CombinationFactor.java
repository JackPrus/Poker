package by.prus;

/**
 * Описывает коэффициент комбинации.
 * Коэффициенты подобраны не случайно. Так комбинация высшего ранга с наинизшим
 * набором карт должна иметь приоритет над комбинацией низшего ранга с наивысшим набором карт.
 * К примеру имеем 2 комбинации : |AD AC KS QS JH| и |2D 2C 3S 3H TS
 * Рейтинг комбинаций рассчитывается в методе int compare() класса PokerHandComparator
 * Кобинация имеющая одну пару тузов - будет наивысшей в своей категории
 * (14+14)*20 = 560 протоив (2+2+3+3)*60 = 600
 * Как видно из примера кобинация высшего ранга низшего набора карт имеет больше "очков".
 *
 * @see PokerHandComparator
 */
public enum CombinationFactor {
    HIGH_CARD(1),
    PAIR(20),
    TWO_PAIR(60),
    THREE(560),
    STRAIGHT(1_180),
    FLUSH(3_600),
    FULL_HOUSE(18_200),
    FOUR(155_000),
    STRAIGHT_FLUSH(440_000);

    private int facor;

    CombinationFactor(int facor) {
        this.facor = facor;
    }

    public int getFacor() { return facor; }
}
