package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;
import java.util.Stack;

public class StateTracker {
    private State board;
    private ArrayList<Card>[] foundation;
    private ArrayList<Card>[] tableau;
    private ArrayList<Card> deck;
    private ArrayList<Card> discard;

    public StateTracker(){
        initState();
    }
    public void initState(){
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
            System.out.println("Deck contains: " + deck.size() + " cards"); //Debugging info only
            k++;
        }
    }

    public void showTopCard(){
        for(int i = 0; i < 7; i++){
            System.out.println(tableau[i] + "\t coord: (" + i + ", " + i + ")");
        }
    }
}
