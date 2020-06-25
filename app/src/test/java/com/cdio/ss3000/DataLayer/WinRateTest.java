package com.cdio.ss3000.DataLayer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;


public class WinRateTest {
    GameControl gameControl;
    State state, oldState;
    State dummyState;
    ArrayList<Card> deck;
    private static final int SMALLESTCARD = 1;
    private static final int LARGESTCARD = 0;
    int[] visible = {0, 1, 2, 3, 4, 5, 6};
    int ITERATIONS = 10000;
    Card move;
    int movesdone, stockmoves;

    @Test
    public void calcWinRate() {
        int wins = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            gameControl = new GameControl(null);
            initDeck();
            Collections.shuffle(deck);

            initState();


            // Setup dummy state
            dummyState = new State(new ArrayList[4], new ArrayList[7], new ArrayList<Card>(), new ArrayList<Card>());

            for (int index = 0; index < dummyState.foundations.length; index++) {
                dummyState.foundations[index] = new ArrayList<>();
            }

            for (int index = 0; index < dummyState.tableau.length; index++) {
                dummyState.tableau[index] = new ArrayList<>();
                dummyState.tableau[index].add(state.tableau[index].get(state.tableau[index].size() - 1));
                dummyState.tableau[index].add(state.tableau[index].get(state.tableau[index].size() - 1));
            }

            movesdone = 0;
            stockmoves = 0;
            for (int j = 0; j < visible.length; j++){
                visible[j] = j;
            }
            for (int moves = 0; moves < 1000; moves++) {

                movesdone++;
                gameControl.updateState(dummyState);

                move = gameControl.run();

                if (move.getValue() < 0) {
                    if (move.getValue() == -1)
                        wins++;
                    break;
                }

                oldState = (State) state.clone();

                updateState(move);

                for (int index = 0, j = 0; index < dummyState.tableau.length; index++, j++) {
                    dummyState.tableau[index] = new ArrayList<>();
                    if (!state.tableau[index].isEmpty()) {
                        dummyState.tableau[j].add(state.tableau[index].get(visible[index]));
                        dummyState.tableau[j].add(state.tableau[index].get(state.tableau[index].size() - 1));
                    } else
                        j--;
                }


                for (int index = 0; index < dummyState.foundations.length; index++) {
                    dummyState.foundations[index] = new ArrayList<>();
                    if (!state.foundations[index].isEmpty()) {
                        dummyState.foundations[index].add(state.foundations[index].get(state.foundations[index].size() - 1));
                        dummyState.foundations[index].add(state.foundations[index].get(state.foundations[index].size() - 1));
                    }
                }

                dummyState.waste = new ArrayList<>();
                if (!state.waste.isEmpty()) {
                    dummyState.waste.add(state.waste.get(state.waste.size() - 1));
                    dummyState.waste.add(state.waste.get(state.waste.size() - 1));
                }

            }


        }

        System.out.println("We won " + wins + " times out of " + ITERATIONS + " games");
    }

    private void initDeck() {
        deck = new ArrayList<>();
        for (Suit suit = Suit.HEARTS; suit.ordinal() <= 4; suit = suit.values()[suit.ordinal() + 1]) {
            for (int val = 1; val <= 13; val++) {
                deck.add(new Card(val, suit));
            }
        }
    }

    private void initState() {
        state = new State(new ArrayList[4], new ArrayList[7], new ArrayList<Card>(), new ArrayList<Card>());

        //Create the deck of cards
        state.stock = deck;

        //Fill the board with cards
        int k = 1;
        for (int i = 0; i < 7; i++) {
            ArrayList<Card> temp_list = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                temp_list.add(state.stock.get(0));
                state.stock.remove(0);
            }
            state.tableau[i] = temp_list;
            //System.out.println("Deck contains: " + deck.size() + " cards"); //Debugging info only
            k++;
        }
        for (int i = 0; i < state.foundations.length; i++) {
            state.foundations[i] = new ArrayList<>();
        }
    }


    public void updateState(Card move) {

        // King cases
        if (move.getValue() == 13) {

            Card destination = move.getMoves().get(0).get(move.getMoves().get(0).size() - 1);

            // King to foundation from tableau
            if (destination.getSuit() == move.getSuit() && destination.getValue() == move.getValue() - 1) {
                int x = 0;
                outerloop:
                for (int i = 0; i < state.foundations.length; i++) {
                    for (Card card : state.foundations[i]) {
                        if (card.compareTo(move.getMoves().get(0).get(0)) == 0) {
                            x = i;
                            break outerloop;
                        }
                    }
                }

                outerloop:
                for (int i = 0; i < state.tableau.length; i++) {
                    for (int j = 0; j < state.tableau[i].size(); j++) {
                        Card card = state.tableau[i].get(j);
                        if (card.compareTo(move) == 0) {
                            state.foundations[x].add(state.tableau[i].remove(j));
                            if (j == visible[i] && visible[i] != 0)
                                visible[i]--;
                            break outerloop;
                        }

                    }
                }
                return;
            }

            // King to tableau
            int x = 0;
            for (int i = 0; i < state.tableau.length; i++) {
                if (state.tableau[i].isEmpty()) {
                    x = i;
                    break;
                }
            }

            // King from waste to tableau
            if (!state.waste.isEmpty() && state.waste.get(state.waste.size() - 1).compareTo(move) == 0) {
                state.tableau[x].add(state.waste.remove(state.waste.size() - 1));
                return;

            }

            // King from tableau to tableau
            outerloop:
            for (int i = 0; i < state.tableau.length; i++) {
                for (int j = 0; j < state.tableau[i].size(); j++) {
                    Card card = state.tableau[i].get(j);
                    if (card.compareTo(move) == 0) {
                        while (true) {
                            try {
                                state.tableau[x].add(state.tableau[i].remove(j));
                            } catch (IndexOutOfBoundsException e) {
                                break;
                            }
                        }
                        if (j == visible[i] && visible[i] != 0)
                            visible[i]--;
                        break outerloop;
                    }

                }
            }
            return;

        }


        // Flip stock card
        if (move.getSuit() == Suit.STOCK) {
            if (state.stock.isEmpty() && state.waste.isEmpty()) {
                return;
            }

            if (state.stock.isEmpty()) {
                while (true) {
                    try {
                        state.stock.add(state.waste.remove(state.waste.size() - 1));
                    } catch (IndexOutOfBoundsException e) {
                        break;
                    }
                }
            }

            state.waste.add(state.stock.remove(state.stock.size() - 1));
            stockmoves++;
            return;
        }

        // Ace to foundation from waste
        if (!state.waste.isEmpty()) {
            if (move.getMoves().isEmpty() && move.compareTo(state.waste.get(state.waste.size() - 1)) == 0) {
                state.foundations[move.getSuit().ordinal() - 1].add(state.waste.remove(state.waste.size() - 1));
                return;
            }
        }

        // Ace to foundation
        if (move.getMoves().isEmpty()) {
            for (int i = 0; i < 7; i++) {
                ArrayList<Card> tableauColumn = state.tableau[i];
                if (!tableauColumn.isEmpty()) {
                    if (move.compareTo(tableauColumn.get(tableauColumn.size() - 1)) == 0) {
                        state.foundations[move.getSuit().ordinal() - 1].add(tableauColumn.remove(tableauColumn.size() - 1));
                        if (visible[i] != 0)
                            visible[i]--;
                        break;
                    }
                }
            }
            return;
        }

        if (!state.waste.isEmpty()) {
            // Waste card to foundation
            if (move.compareTo(state.waste.get(state.waste.size() - 1)) == 0 &&
                    move.getMoves().get(0).get(0).getSuit() == move.getSuit()) {
                state.foundations[move.getSuit().ordinal() - 1].add(state.waste.remove(state.waste.size() - 1));
                return;
            }

            // Waste card to card
            if (move.compareTo(state.waste.get(state.waste.size() - 1)) == 0 &&
                    move.getMoves().get(0).get(0).isRed() != move.isRed()) {
                int x = 0;
                outerloop:
                for (int i = 0; i < state.tableau.length; i++) {
                    for (Card card : state.tableau[i]) {
                        if (card.compareTo(move.getMoves().get(0).get(0)) == 0) {
                            x = i;
                            break outerloop;
                        }
                    }
                }
                state.tableau[x].add(state.waste.remove(state.waste.size() - 1));
                return;
            }
        }

        // Card to foundation
        if (move.getMoves().get(0).get(0).getSuit() == move.getSuit()) {
            int x = 0;
            outerloop:
            for (int i = 0; i < state.foundations.length; i++) {
                for (Card card : state.foundations[i]) {
                    if (card.compareTo(move.getMoves().get(0).get(0)) == 0) {
                        x = i;
                        break outerloop;
                    }
                }
            }

            outerloop:
            for (int i = 0; i < state.tableau.length; i++) {
                for (int j = 0; j < state.tableau[i].size(); j++) {
                    Card card = state.tableau[i].get(j);
                    if (card.compareTo(move) == 0) {
                        state.foundations[x].add(state.tableau[i].remove(j));
                        if (j == visible[i] && visible[i] != 0)
                            visible[i]--;
                        break outerloop;
                    }

                }
            }

            return;
        }


        // Card to card
        if (move.getMoves().get(0).get(0).isRed() != move.isRed()) {
            int x = 0;
            outerloop:
            for (int i = 0; i < state.tableau.length; i++) {
                for (Card card : state.tableau[i]) {
                    if (card.compareTo(move.getMoves().get(0).get(0)) == 0) {
                        x = i;
                        break outerloop;
                    }
                }
            }


            outerloop:
            for (int i = 0; i < state.tableau.length; i++) {
                for (int j = 0; j < state.tableau[i].size(); j++) {
                    Card card = state.tableau[i].get(j);
                    if (card.compareTo(move) == 0) {
                        while (true) {
                            try {
                                state.tableau[x].add(state.tableau[i].remove(j));
                            } catch (IndexOutOfBoundsException e) {
                                break;
                            }
                        }
                        if (j == visible[i] && visible[i] != 0)
                            visible[i]--;
                        break outerloop;
                    }

                }
            }
            return;

        }

        return;

    }

}
