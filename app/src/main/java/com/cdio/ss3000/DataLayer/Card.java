package com.cdio.ss3000.DataLayer;

import androidx.annotation.NonNull;

import java.util.LinkedList;

public class Card {
    int value; // From 1 (ace) to 13 (king) (0 is unknown)
    Suit suit;
    boolean movable;
    LinkedList<Card> moves;
    LinkedList<LinkedList> emptySpaceTableau;
    LinkedList<LinkedList> emptySpaceFoundation;

    private boolean isPlacedInFoundation;
    private Card topCard, bottomCard;

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

    public void clearMoves(){
        moves.clear();
        emptySpaceTableau.clear();
    }

    public LinkedList<Card> getMoves(){return moves;}

    public void addMoveToEmptySpaceTableau(LinkedList<Card> space){emptySpaceTableau.add(space);}

    public void addMoveToEmptySpaceFoundation(LinkedList<Card> space){emptySpaceFoundation.add(space);}

    public LinkedList<LinkedList> getMovesToEmptySpaceTableau(){return emptySpaceTableau;}

    public LinkedList<LinkedList> getMovesToEmptySpaceFoundation(){return emptySpaceFoundation;}

    public void clearMovesToEmptySpace(){emptySpaceTableau.clear();}

    public boolean getPlacedInFoundation(){return isPlacedInFoundation;}

    public void setPlacedInFoundation(boolean isPlacedInFoundation){this.isPlacedInFoundation = isPlacedInFoundation;}

    public void setTopCard(Card topCard){this.topCard = topCard;}

    public Card getTopCard() {return topCard;}

    public void setBottomCard(Card bottomCard){this.bottomCard = bottomCard;}

    public Card getBottomCard(){return bottomCard;}


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Card newCard = new Card(this.value, this.suit, this.movable);
        return newCard;
    }
}
