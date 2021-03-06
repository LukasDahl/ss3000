package com.cdio.ss3000.DataLayer;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;

public class Card implements Comparable{
    private int value; // From 1 (ace) to 13 (king) (0 is unknown)
    private Suit suit;
    private ArrayList<ArrayList<Card>> moves = new ArrayList<>();
    private int points = 0;

    private boolean isPlacedInFoundation, isRed, isWaste;

    public Card(int value, Suit suit) {
        this.value = value;
        this.suit = suit;

        if (this.suit == Suit.SPADES || this.suit == Suit.CLUBS) isRed = false;
        else if (this.suit == Suit.HEARTS || this.suit == Suit.DIAMONDS) isRed = true;
    }

    public Card() {
        this.value = 0;
        this.suit = Suit.UNKNOWN;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int p) {
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
        if (this.suit == Suit.SPADES || this.suit == Suit.CLUBS) isRed = false;
        else if (this.suit == Suit.HEARTS || this.suit == Suit.DIAMONDS) isRed = true;
    }


    public void addMove(ArrayList<Card> possibleMoveTo) {
        moves.add(possibleMoveTo);
    }

    public void clearMoves() {
        moves.clear();
    }

    public ArrayList<ArrayList<Card>> getMoves() {
        return moves;
    }

    public boolean getPlacedInFoundation() {
        return isPlacedInFoundation;
    }

    public void setPlacedInFoundation(boolean isPlacedInFoundation) {
        this.isPlacedInFoundation = isPlacedInFoundation;
    }

    public boolean isRed() {
        return isRed;
    }

    public boolean getWaste() {
        return isWaste;
    }

    public void setWaste(boolean waste) {
        isWaste = waste;
    }

    @NonNull
    @Override
    protected Object clone() {
        Card newCard = new Card(this.value, this.suit);
        return newCard;
    }

    public String toString() {
        String str = "";
        switch (getValue()) {
            case 1:
                str += "Value: Ace\t";
                break;
            case 11:
                str += "Value: Jack\t";
                break;
            case 12:
                str += "Value: Queen\t";
                break;
            case 13:
                str += "Value: King\t";
                break;

            default:
                str += "Value: " + getValue() + "\t";
        }
        str += "Suit: " + getSuit() + "\t";
        str += "Points: " + getPoints();
        return str;
    }

    public String toMovesString() {

        String str = "";

        if (getSuit() == Suit.STOCK){
            str = "Turn card from stock";
            //str += " for " + points + " points.";
            return str;
        }

        switch (getValue()) {
            case 1:
                str += "Ace";
                break;
            case 11:
                str += "Jack";
                break;
            case 12:
                str += "Queen";
                break;
            case 13:
                str += "King";
                break;
            default:
                str += getValue();
        }
        str += " of ";
        switch (getSuit()) {
            case HEARTS:
                str += "hearts";
                break;
            case CLUBS:
                str += "clubs";
                break;
            case DIAMONDS:
                str += "diamonds";
                break;
            case SPADES:
                str += "spades";
                break;
        }
        str += " → ";

        if (getMoves().isEmpty()){
            str += "Foundation";
            //str += " for " + points + " points.";
            return str;
        }

        Card destination = getMoves().get(getMoves().size() - 1).get(getMoves().get(getMoves().size() - 1   ).size() - 1);

        if (getValue() == 13){
            if (!(destination.getValue() == 12) || !(destination.getSuit() == getSuit())){
                 str += "Left-most empty spot";
                 //str += " for " + points + " points.";
                 return str;
            }
        }


        switch (destination.getValue()) {
            case 1:
                str += "Ace";
                break;
            case 11:
                str += "Jack";
                break;
            case 12:
                str += "Queen";
                break;
            case 13:
                str += "King";
                break;
            default:
                str += destination.getValue();
        }
        str += " of ";
        switch (destination.getSuit()) {
            case HEARTS:
                str += "hearts";
                break;
            case CLUBS:
                str += "clubs";
                break;
            case DIAMONDS:
                str += "diamonds";
                break;
            case SPADES:
                str += "spades";
                break;
        }
        //str += " for " + points + " points.";
        return str;
    }

    @Override
    public int compareTo(Object o) {
        return (getValue() + (13 * getSuit().ordinal())) - (((Card)o).getValue() + (13 * ((Card)o).getSuit().ordinal()));
    }
}
