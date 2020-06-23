package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;

/**
 * @Author Flotfyr27 - https://github.com/Flotfyr27
 */
public class StateTracker {
    private static final int SMALLESTCARD = 1;
    private static final int LARGESTCARD = 0;
    private State board;
    private ArrayList<Card>[] foundation;
    private ArrayList<Card>[] tableau;
    private ArrayList<Card> stock;
    private ArrayList<Card> waste;

    public Status gameOver(){
        boolean won = true;
        boolean lost = true;
        for (ArrayList<Card> foundationPile: foundation){
            if (foundationPile.isEmpty() || foundationPile.get(foundationPile.size() - 1).getValue() != 13){
                won = false;
            }
        }
        if (won)
            return Status.WON;

        for(Card wasteCard : waste){
            if(!wasteCard.getMoves().isEmpty()) lost = false;

        }
        for(Card stockCard : stock){
            if(!stockCard.getMoves().isEmpty()) lost = false;
        }
        if(lost)
            return Status.LOST;

        // TODO: CHECK FOR LOSS




        return Status.INPROGRESS;
    }

    public StateTracker() {
        initState();
    }

    private void initState() {
        resetBoard();
        board = new State(foundation, tableau, stock, waste);
    }

    private void resetBoard() {
        //Init required fields
        foundation = new ArrayList[4];
        tableau = new ArrayList[7];
        waste = new ArrayList<>();
        //Create the deck of cards
        stock = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            stock.add(new Card());
        }

        //Fill the board with cards
        int k = 1;
        for (int i = 0; i < 7; i++) {
            ArrayList<Card> temp_list = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                temp_list.add(stock.get(0));
                stock.remove(0);
            }
            tableau[i] = temp_list;
            //System.out.println("Deck contains: " + deck.size() + " cards"); //Debugging info only
            k++;
        }
        for (int i = 0; i < foundation.length; i++) {
            foundation[i] = new ArrayList<>();
        }
    }

    public void updateState(State inputState, Card lastMove) {
        ArrayList<Card>[] tempTableau;

        // Initial setup
        if (lastMove == null) {

            //DEBUG TODO: REMOVE THIS
            if (false) {
                for (int i = 0; i < 7; i++) {
                    tableau[i].get(tableau[i].size() - 1).setSuit(Suit.HEARTS);//Set suit
                    tableau[i].get(tableau[i].size() - 1).setValue(5);//Set value

                }
                int i = 2;
                while (!stock.isEmpty()) {
                    Card card = stock.remove(stock.size() - 1);
                    card.setSuit(Suit.HEARTS);
                    card.setValue(i);
                    waste.add(card);
                    i++;
                }
                waste.get(waste.size()-1).setValue(0);
                waste.get(waste.size()-1).setSuit(Suit.UNKNOWN);
                stock.add(waste.remove(waste.size()-1));
                return;
            }
            //First time run -> Cards are all unknown
            for (int i = 0; i < 7; i++) {
                Card smallest = inputState.tableau[i].get(SMALLESTCARD);
                Card largest = inputState.tableau[i].get(LARGESTCARD);
                if (smallest.getSuit() == largest.getSuit() &&
                        smallest.getValue() == largest.getValue()) {//Check if both highest and lowest card is the same card.
                    //"Turns" the card so it is now face-up.
                    tableau[i].get(tableau[i].size() - 1).setSuit(smallest.getSuit());//Set suit
                    tableau[i].get(tableau[i].size() - 1).setValue(smallest.getValue());//Set value
                }
            }
            return;
        }


        // King cases
        if (lastMove.getValue() == 13) {
            Card destination = lastMove.getMoves().get(0).get(lastMove.getMoves().get(0).size() - 1);
            if (destination.getSuit() == lastMove.getSuit() && destination.getValue() == 12) {
                for (int i = 0; i < foundation.length; i++) {
                    if (!foundation[i].isEmpty()) {
                        if (destination.compareTo(foundation[i].get(foundation[i].size() - 1)) == 0) {

                            //TODO KING TO FOUNDATION
                            return;
                        }
                    }
                }
            }


            int x = 0;
            for (int i = 0; i < inputState.tableau.length; i++) {
                if (!inputState.tableau[i].isEmpty() && inputState.tableau[i].get(LARGESTCARD).compareTo(lastMove) == 0) {
                    x = i;
                    break;
                }
            }

            if (!waste.isEmpty() && waste.get(waste.size() - 1).compareTo(lastMove) == 0) {
                tableau[x].add(waste.remove(waste.size() - 1));
                return;
            }


            for (int i = 0; i < tableau.length; i++) {
                for (int j = 0; j < tableau[i].size(); j++) {
                    Card card = tableau[i].get(j);
                    if (card.compareTo(lastMove) == 0) {
                        while (true) {
                            try {
                                tableau[x].add(tableau[i].remove(j));
                            } catch (IndexOutOfBoundsException e) {
                                break;
                            }
                        }

                        if (!tableau[i].isEmpty()) {
                            if (tableau[i].get(tableau[i].size() - 1).getSuit() == Suit.UNKNOWN) {
                                tempTableau = getTempTableau(inputState.tableau);
                                tableau[i].get(tableau[i].size() - 1).setValue(tempTableau[i].get(SMALLESTCARD).getValue());
                                tableau[i].get(tableau[i].size() - 1).setSuit(tempTableau[i].get(SMALLESTCARD).getSuit());
                            }
                        }
                        break;
                    }

                }
            }
            return;

        }
        // Flip stock card
        if (lastMove.getSuit() == Suit.STOCK) {
            if (stock.isEmpty() && waste.isEmpty())
                return;

            if (stock.isEmpty()) {
                while (true) {
                    try {
                        stock.add(waste.remove(waste.size() - 1));
                    } catch (IndexOutOfBoundsException e) {
                        break;
                    }
                }
            }

            waste.add(stock.remove(stock.size() - 1));

            if (waste.get(waste.size() - 1).getSuit() == Suit.UNKNOWN) {
                Card newWasteCard = inputState.waste.get(0);
                waste.get(waste.size() - 1).setSuit(newWasteCard.getSuit());
                waste.get(waste.size() - 1).setValue(newWasteCard.getValue());
            }
            System.out.println("Stock: " + stock.size());
            return;
        }

        // Ace to foundation from waste
        if (!waste.isEmpty()) {
            if (lastMove.getMoves().isEmpty() && lastMove.compareTo(waste.get(waste.size() - 1)) == 0) {
                System.arraycopy(inputState.foundations, 0, foundation, 0, 4);
                waste.remove(waste.size() - 1);
                return;
            }
        }

        // Ace to foundation
        if (lastMove.getMoves().isEmpty()) {
            System.arraycopy(inputState.foundations, 0, foundation, 0, 4);
            for (int i = 0; i < 7; i++) {
                ArrayList<Card> tableauColumn = tableau[i];

                if (!tableauColumn.isEmpty()) {
                    if (lastMove.compareTo(tableauColumn.get(tableauColumn.size() - 1)) == 0) {
                        tableauColumn.remove(tableauColumn.size() - 1);
                        if (!tableauColumn.isEmpty()) {
                            if (tableau[i].get(tableau[i].size() - 1).getSuit() == Suit.UNKNOWN) {
                                tempTableau = getTempTableau(inputState.tableau);
                                tableauColumn.get(tableauColumn.size() - 1).setValue(tempTableau[i].get(SMALLESTCARD).getValue());
                                tableauColumn.get(tableauColumn.size() - 1).setSuit(tempTableau[i].get(SMALLESTCARD).getSuit());
                            }
                        }
                        break;
                    }
                }
            }
            return;
        }

        // Waste card to foundation
        if (!waste.isEmpty()) {
            if (lastMove.compareTo(waste.get(waste.size() - 1)) == 0 &&
                    lastMove.getMoves().get(0).get(0).getSuit() == lastMove.getSuit()) {
                System.arraycopy(inputState.foundations, 0, foundation, 0, 4);
                waste.remove(waste.size() - 1);
                return;
            }

            // Waste card to card
            if (lastMove.compareTo(waste.get(waste.size() - 1)) == 0 &&
                    lastMove.getMoves().get(0).get(0).isRed() != lastMove.isRed()) {
                int x = 0;
                outerloop:
                for (int i = 0; i < tableau.length; i++) {
                    for (Card card : tableau[i]) {
                        if (card.compareTo(lastMove.getMoves().get(0).get(0)) == 0) {
                            x = i;
                            break outerloop;
                        }
                    }
                }
                tableau[x].add(waste.remove(waste.size() - 1));
                return;
            }
        }

        // Card to foundation
        if (lastMove.getMoves().get(0).get(0).getSuit() == lastMove.getSuit()) {
            System.arraycopy(inputState.foundations, 0, foundation, 0, 4);
            for (int i = 0; i < 7; i++) {
                ArrayList<Card> tableauColumn = tableau[i];

                if (!tableauColumn.isEmpty()) {
                    if (lastMove.compareTo(tableauColumn.get(tableauColumn.size() - 1)) == 0) {
                        tableauColumn.remove(tableauColumn.size() - 1);
                        if (!tableauColumn.isEmpty()) {
                            if (tableau[i].get(tableau[i].size() - 1).getSuit() == Suit.UNKNOWN) {
                                tempTableau = getTempTableau(inputState.tableau);
                                tableauColumn.get(tableauColumn.size() - 1).setValue(tempTableau[i].get(SMALLESTCARD).getValue());
                                tableauColumn.get(tableauColumn.size() - 1).setSuit(tempTableau[i].get(SMALLESTCARD).getSuit());
                            }
                        }
                        break;
                    }
                }
            }
            return;
        }


        // Card to card
        if (lastMove.getMoves().get(0).get(0).isRed() != lastMove.isRed()) {
            int x = 0;
            outerloop:
            for (int i = 0; i < tableau.length; i++) {
                for (Card card : tableau[i]) {
                    if (card.compareTo(lastMove.getMoves().get(0).get(0)) == 0) {
                        x = i;
                        break outerloop;
                    }
                }
            }


            outerloop:
            for (int i = 0; i < tableau.length; i++) {
                for (int j = 0; j < tableau[i].size(); j++) {
                    Card card = tableau[i].get(j);
                    if (card.compareTo(lastMove) == 0) {
                        while (true) {
                            try {
                                tableau[x].add(tableau[i].remove(j));
                            } catch (IndexOutOfBoundsException e) {
                                break;
                            }
                        }

                        if (!tableau[i].isEmpty()) {
                            if (tableau[i].get(tableau[i].size() - 1).getSuit() == Suit.UNKNOWN) {
                                tempTableau = getTempTableau(inputState.tableau);
                                tableau[i].get(tableau[i].size() - 1).setValue(tempTableau[i].get(SMALLESTCARD).getValue());
                                tableau[i].get(tableau[i].size() - 1).setSuit(tempTableau[i].get(SMALLESTCARD).getSuit());
                            }
                        }
                        break outerloop;
                    }

                }
            }
            return;

        }


    }

    public void showTopCard() {
        System.out.println("-----\tTableau\t-----");
        for (int i = 0; i < 7; i++) {
            System.out.println(tableau[i] + "\t coord: (" + i + ", " + i + ")");
        }
        System.out.println("-----\tFoundation\t-----");
        for (int i = 0; i < 4; i++) {
            System.out.println(foundation[i] + "\t index: " + i);
        }
    }

    public ArrayList<Card>[] getTableau() {
        return tableau;
    }

    public State getBoard() {
        try {
            return (State) board.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Card>[] getTempTableau(ArrayList<Card>[] inputTableau) {
        ArrayList<Card>[] tempTableau = new ArrayList[7];
        for (int i = 0, j = 0; i < tableau.length; i++, j++) {
            if (tableau[i].isEmpty()) {
                tempTableau[i] = new ArrayList<>();
                j--;
                continue;
            }
            tempTableau[i] = inputTableau[j];
        }
        return tempTableau;
    }
}
