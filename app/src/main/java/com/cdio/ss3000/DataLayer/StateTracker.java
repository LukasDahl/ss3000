package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @Author Flotfyr27 - https://github.com/Flotfyr27
 */
public class StateTracker {
    private State board;
    private ArrayList<Card>[] foundation;
    private ArrayList<Card>[] tableau;
    private ArrayList<Card> deck;
    private ArrayList<Card> discard;

    public StateTracker(){
        initState();
    }
    private void initState(){
        resetBoard();
        board = new State(foundation, tableau, deck, discard);
    }
    private void resetBoard(){
        //Init required fields
        foundation  = new ArrayList[4];
        tableau = new ArrayList[7];
        discard = new ArrayList<>();
        //Create the deck of cards
        deck = new ArrayList<>();
        for(int i = 0; i < 52; i++){
            deck.add(new Card());
        }

        //Fill the board with cards
        int k = 1;
        for(int i = 0; i < 7; i++){
            ArrayList<Card> temp_list = new ArrayList<>();
            for(int j = 0; j < k; j++){
                temp_list.add(deck.get(0));
                deck.remove(0);
            }
            tableau[i] = temp_list;
            //System.out.println("Deck contains: " + deck.size() + " cards"); //Debugging info only
            k++;
        }
    }

    public void updateState(State inputState){
        int columnIndex = 0;
        //Index 0 is the card with highest value, index 1 is the card with lowest value

        //First time run -> Cards are all unknown
        for(int i = 0; i < 7; i++){
            if(tableau[i].get(tableau[i].size()-1).getSuit() == Suit.UNKNOWN){//Check if the top card in tableau is a face down card
                if(inputState.tableau[i].get(1).equals(inputState.tableau[i].get(0))){//Check if both highest and lowest card is the same card.
                    Card newCard = inputState.tableau[i].get(1);//Save the card to be used
                    //"Turns" the card so it is now face-up.
                    tableau[i].get(tableau[i].size()-1).setSuit(newCard.getSuit());//Set suit
                    tableau[i].get(tableau[i].size()-1).setValue(newCard.getValue());//Set value
                    tableau[i].get(tableau[i].size()-1).setMovable(true);//Set movable
                }
                if(i == 6) return;//Stop for the first time run
            }
        }

        //Assume at least one card is face-up in every tableau column
        for(int i = 0; i < 7; i++){
            //If the value of the updated card is lower than the current card, then the length of the column has increased
            if(tableau[i].get(tableau[i].size()-1).getValue() > inputState.tableau[i].get(1).getValue()){
                tableau[i].add(inputState.tableau[i].get(1));//Adds the card with the lowest value (the one in the front) to our registered tableau.
            }else{//If not then the column length has decreased

            }
        }

    }

    public void showTopCard(){
        for(int i = 0; i < 7; i++){
            System.out.println(tableau[i] + "\t coord: (" + i + ", " + i + ")");
        }
    }
}
