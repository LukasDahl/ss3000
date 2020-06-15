package com.cdio.ss3000.DataLayer;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;

public class Card {
    private int value; // From 1 (ace) to 13 (king) (0 is unknown)
    private Suit suit;
    private boolean movable;
    private ArrayList<ArrayList> moves = new ArrayList<>();
    //LinkedList<LinkedList> emptySpaceTableau;
    //LinkedList<LinkedList> emptySpaceFoundation;
    private int points = 0;

    private boolean isPlacedInFoundation, isRed;
    private Card topCard, bottomCard;

    public Card(int value, Suit suit, boolean movable) {
        this.value = value;
        this.suit = suit;
        this.movable = movable;

        if(this.suit == suit.SPADES || this.suit == suit.CLUBS ) isRed = false;
        else if(this.suit == suit.HEARTS || this.suit == suit.HEARTS) isRed = true;
    }

    public Card() {
        this.value = 0;
        this.suit = Suit.UNKNOWN;
        this.movable = false;
    }
    public int getPoints(){
        return points;
    }

    public void setPoints(int p){
        points = p;
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

    public void addMove(ArrayList<Card> possibleMoveTo){
        moves.add(possibleMoveTo);
    }

    public void clearMoves(){
        moves.clear();
    //    emptySpaceTableau.clear();
    }

    public ArrayList<ArrayList> getMoves(){return moves;}

    //public void addMoveToEmptySpaceTableau(LinkedList<Card> space){emptySpaceTableau.add(space);}

    //public void addMoveToEmptySpaceFoundation(LinkedList<Card> space){emptySpaceFoundation.add(space);}

   // public LinkedList<LinkedList> getMovesToEmptySpaceTableau(){return emptySpaceTableau;}

   // public LinkedList<LinkedList> getMovesToEmptySpaceFoundation(){return emptySpaceFoundation;}

   // public void clearMovesToEmptySpace(){emptySpaceTableau.clear();}

    public boolean getPlacedInFoundation(){return isPlacedInFoundation;}

    public void setPlacedInFoundation(boolean isPlacedInFoundation){this.isPlacedInFoundation = isPlacedInFoundation;}

    public void setTopCard(Card topCard){this.topCard = topCard;}

    public Card getTopCard() {return topCard;}

    public void setBottomCard(Card bottomCard){this.bottomCard = bottomCard;}

    public Card getBottomCard(){return bottomCard;}

    public boolean isRed(){return isRed;}


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Card newCard = new Card(this.value, this.suit, this.movable);
        return newCard;
    }

    public String toString(){
        String str = "";
        switch (getValue()){
            case 1: str += "Value: Ace\t"; break;
            case 11: str += "Value: Jack\t"; break;
            case 12: str += "Value: Queen\t"; break;
            case 13: str += "Value: King\t"; break;

            default: str += "Value: " + getValue() + "\t";
        }
        str += "Suit: " + getSuit() + "\t";
        str += "Points: " + getPoints() + "\t";
        str += "Movable: " + isMovable();
        return str;
    }
}
