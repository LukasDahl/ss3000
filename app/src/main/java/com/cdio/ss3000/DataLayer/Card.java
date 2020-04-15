package com.cdio.ss3000.DataLayer;

import androidx.annotation.NonNull;

import java.util.LinkedList;

public class Card {
    int value; // From 1 (ace) to 13 (king) (0 is unknown)
    Suit suit;
    boolean movable;
    LinkedList<Card> moves;

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

    public void addMove(Card possibleMoveTo){moves.push(possibleMoveTo);}

    public LinkedList<Card> getMoves(){return moves;}

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Card newCard = new Card(this.value, this.suit, this.movable);
        return newCard;
    }
}
