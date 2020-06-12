package com.cdio.ss3000.Vision;

import com.cdio.ss3000.DataLayer.Card;
import com.cdio.ss3000.DataLayer.State;
import com.cdio.ss3000.DataLayer.Suit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pile implements Comparable {

    int x, y;
    List<Integer> cards;
    static boolean checkTop = false, flip = false;

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

    public static int valueOfCard(int card) {
        int value = card % 13;
        if (value == 0) {
            value = 13;
        }
        value = 14 - value;
        return value;
    }


    @Override
    public int compareTo(Object o) {
        return x - ((Pile) o).getX();
    }

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

        // check once if stok is in bottom pile
        if (!checkTop) {
            for (Pile flop : pilesBottom) {
                if (flop.cards.size() == 0) {
                    flip = true;
                    break;
                }
            }
        }
        // flip top and bottom if stok is in bottom pile
        if (flip) {
            List<Pile> tempPile = new ArrayList<>();
            tempPile = pilesBottom;
            pilesBottom = pilesTop;
            pilesTop = tempPile;

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
        LinkedList<Card>[] foundations = new LinkedList[4], tableau = new LinkedList[7];
        LinkedList<Card> stock = new LinkedList<>();
        LinkedList<Card> waste = new LinkedList<>();

        for (int i = 0; i < 4; i++) {
            foundations[i] = new LinkedList<>();
        }

        Pile stockPile = null;
        Pile wastePile = null;


        //FIND EMPTY PILE
        for (int i = 0; i < pilesTop.size(); i++) {
            if (pilesTop.get(i).cards.size() == 0) {
                stockPile = pilesTop.get(i);
            }
        }

        //FIND WASTE WHICH IS CLOSE TO STOCK
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
            }

        }


        for (int i = 0; i < pilesBottom.size(); i++) {
            tableau[i] = new LinkedList<>();
            tableau[i].add(intToCard(pilesBottom.get(i).largestCard()));
            tableau[i].add(intToCard(pilesBottom.get(i).smallestCard()));
        }


        State state = new State(foundations, tableau, stock, waste);
        System.out.println(state.toString());
        return state;
    }
}
