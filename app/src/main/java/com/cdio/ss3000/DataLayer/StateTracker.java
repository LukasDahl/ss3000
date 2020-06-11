package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;

public class StateTracker {
    private Card[][] board;

    public StateTracker(){
        resetBoard();
    }
    private void resetBoard(){
        board = new Card[7][20];
        int k = 1;
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < k; j++){
                board[i][j] = new Card();
            }
            k++;
        }
    }
}
