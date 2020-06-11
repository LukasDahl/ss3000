package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class StateTracker {
    private Card[][] board;
    private Stack<Card> deck;

    public StateTracker(){
        resetBoard();
    }
    private void resetBoard(){
        //Create the deck of cards
        deck = new Stack<>();
        for(int i = 0; i < 52; i++){
            deck.add(new Card());
        }
        //Fill the board with cards
        board = new Card[7][20];
        int k = 1;
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < k; j++){
                board[i][j] = deck.peek();
                deck.pop();
            }
            //Debugging info only
            System.out.println("Deck contains: " + deck.size() + " cards");
            k++;
        }
    }

    public void showTopCard(){
        for(int i = 0; i < 7; i++){
            System.out.println(board[i][i] + "\t coord: (" + i + ", " + i + ")");
        }
    }
}
