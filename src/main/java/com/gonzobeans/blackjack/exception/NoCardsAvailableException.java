package com.gonzobeans.blackjack.exception;

public class NoCardsAvailableException extends RuntimeException {
    public NoCardsAvailableException(String message) {
        super(message);
    }
}
