package com.cdio.ss3000.DataLayer;

import androidx.annotation.NonNull;

public class Card {
    private int value; // From 1 (ace) to 13 (king) (0 is unknown)
    private Suit suit;
    private boolean movable;
    private boolean isPlacedInFoundation;

    public Card(int value, Suit suit, boolean movable) {
        this.value = value;
        this.suit = suit;
        this.movable = movable;
    }

    public Card() {
        this.value = 0;
        this.suit = Suit.UNKNOWN;
        this.movable = false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public boolean getPlacedInFoundation(){return isPlacedInFoundation;}

    public void setPlacedInFoundation(boolean isPlacedInFoundation){this.isPlacedInFoundation = isPlacedInFoundation;}

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Card newCard = new Card(this.value, this.suit, this.movable);
        return newCard;
    }
}
