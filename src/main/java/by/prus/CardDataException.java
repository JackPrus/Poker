package by.prus;

import java.util.function.Supplier;

public class CardDataException extends Exception {

    private String description;

    public CardDataException(String description) {
        super(description);
    }
}