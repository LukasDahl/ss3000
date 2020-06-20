package com.cdio.ss3000.Vision;

import androidx.annotation.NonNull;

import com.cdio.ss3000.DataLayer.Card;
import com.cdio.ss3000.DataLayer.State;
import com.cdio.ss3000.DataLayer.Suit;

import java.util.ArrayList;
import java.util.List;

public class Pile implements Comparable {

    int x, y;
    List<Integer> cards;
    static boolean checkTop = true, flip = false;
    static int fAmount = 0;
    static int stokX;

    public Pile(int x, int y, List<Integer> cards) {
        this.x = x;
        this.y = y;
        this.cards = cards;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Integer> getCards() {
        return cards;
    }


    // Finds the card with the smallest value
    public int smallestCard() {
        int min = 14;
        int returnCard = 0;
        for (int card : cards) {
            int value = valueOfCard(card);
                if (value < min) {
                min = value;
                returnCard = card;
            }
        }
        return returnCard;
    }

    // Finds the card with the biggest value
    public int largestCard() {
        int max = 0;
        int returnCard = 0;
        for (int card : cards) {
            int value = valueOfCard(card);
            if (value > max) {
                max = value;
                returnCard = card;
            }
        }
        return returnCard;
    }

    // Gets the value from the number yolo returns
    public static int valueOfCard(int card) {
        int value = card % 13;
        if (value == 0) {
            value = 13;
        }
        value = 14 - value;
        return value;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        return x - ((Pile) o).getX();
    }

    // Split piles based on y value
    public static void splitPiles(List<Pile> piles, List<Pile> pilesTop, List<Pile> pilesBottom) {
        int min = 1000000, max = -1000000, middle;
        for (Pile pile : piles) {
            if (pile.getY() > max)
                max = pile.getY();
            if (pile.getY() < min)
                min = pile.getY();
        }

        middle = min + ((max - min) / 2);

        for (Pile pile : piles) {
            if (pile.getY() > middle)
                pilesBottom.add(pile);
            else
                pilesTop.add(pile);
        }

        // Check once if stock is in bottom pile
        if (checkTop) {
            for (Pile flop : pilesBottom) {
                if (flop.cards.size() == 0) {
                    flip = true;
                    break;
                }
            }
            checkTop = false;
        }

    }


    public static Card intToCard(int cardID) {
        Card card = new Card();
        if (cardID < 13)
            card.setSuit(Suit.HEARTS);
        else if (cardID < 26)
            card.setSuit(Suit.DIAMONDS);
        else if (cardID < 39)
            card.setSuit(Suit.CLUBS);
        else
            card.setSuit(Suit.SPADES);

        card.setValue(valueOfCard(cardID));

        return card;

    }


    public static State pileListToState(List<Pile> pilesTop, List<Pile> pilesBottom) {

        // Flip top and bottom if stock is in bottom pile
        if (flip) {
            List<Pile> tempPile;
            tempPile = pilesBottom;
            pilesBottom = pilesTop;
            pilesTop = tempPile;
        }

        ArrayList<Card>[] foundations = new ArrayList[4], tableau = new ArrayList[7];
        ArrayList<Card> stock = new ArrayList<>();
        ArrayList<Card> waste = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            foundations[i] = new ArrayList<>();
        }

        Pile stockPile = null;
        Pile wastePile = null;


        // Find empty pile
        for (int i = 0; i < pilesTop.size(); i++) {
            if (pilesTop.get(i).cards.size() == 0) {
                stockPile = pilesTop.get(i);
                stokX = stockPile.x;
            }
        }

        // Find waste which is close to stock
        if (stockPile != null) {
            for (int i = 0; i < pilesTop.size(); i++) {
                if (pilesTop.get(i) == stockPile)
                    continue;

                int x = pilesTop.get(i).getX();
                if (x < stockPile.getX() + 350 && x > stockPile.getX() - 350) {
                    wastePile = pilesTop.get(i);
                    waste.add(intToCard(pilesTop.get(i).largestCard()));
                    break;
                }
            }

            int i = 0;
            for (Pile pile : pilesTop) {
                if (pile == wastePile || pile == stockPile)
                    continue;

                foundations[i].add(intToCard(pile.largestCard()));
                i++;
                if (i > fAmount)
                    fAmount = i;
            }

        } else {

            if(pilesTop.size() > fAmount){

                int smallestLength = Integer.MAX_VALUE;;
                for (Pile pile : pilesTop) {

                    int lenth = Math.abs(pile.x - stokX);

                    if (lenth < smallestLength) {
                        smallestLength = lenth;
                        wastePile = pile;
                    }
                }
                waste.add(intToCard(wastePile.largestCard()));
            }
            int i2 = 0;
            for (Pile pile : pilesTop) {
                if (pile == wastePile)
                    continue;

                foundations[i2].add(intToCard(pile.largestCard()));
                i2++;
            }

        }


        for (int i = 0; i < tableau.length; i++) {
            tableau[i] = new ArrayList<>();
            try {
                tableau[i].add(intToCard(pilesBottom.get(i).largestCard()));
                tableau[i].add(intToCard(pilesBottom.get(i).smallestCard()));
            } catch (IndexOutOfBoundsException e){
                continue;
            }
        }


        State state = new State(foundations, tableau, stock, waste);
        System.out.println(state.toString());
        return state;
    }
}
