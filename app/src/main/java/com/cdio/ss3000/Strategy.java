package com.cdio.ss3000;

import com.cdio.ss3000.DataLayer.Card;
import com.cdio.ss3000.DataLayer.GameControl;
import com.cdio.ss3000.DataLayer.State;

import java.util.LinkedList;

public class Strategy {

    private GameControl gameControl;
    private State state;

    public Object calculateBestMove(){
        Object bestMove = null;

        state = (State)gameControl.getState();
        gameControl.checkPossibleMoves();

        for(Card card : state.getMovableCardsTableau()){
            for(LinkedList<Card> tableauList : state.tableau)
            if(card.getValue() == 12 && card.getMovesToEmptySpace() != null){
                bestMove = card.getMovesToEmptySpace().peek();
            }
        }

        return bestMove;
    }

}
