package com.cdio.ss3000;

import com.cdio.ss3000.DataLayer.Card;
import com.cdio.ss3000.DataLayer.GameControl;
import com.cdio.ss3000.DataLayer.State;


/*
1. Turn up the first card of the deck, before making any other moves
2. Always move ace our deuce to foundation whenever possible
3. Expose hidden cards
4.
 */
public class Strategy {

    private GameControl gameControl;
    private State state;

    public Object calculateBestMoveCard(Card card){
        Object bestMove = null;
/*
        if(!card.getMovesToEmptySpaceFoundation().isEmpty() && card.getValue() == 1){
            bestMove = card.getMovesToEmptySpaceFoundation().peek();
        }

        else if(!card.getMovesToEmptySpaceTableau().isEmpty() && card.getValue() == 13){
            bestMove = card.getMovesToEmptySpaceTableau().peek();
            }

        else if(!card.getMoves().isEmpty()){
            for (Card cardMove : card.getMoves()){
               if(cardMove.getPlacedInFoundation()){
                   bestMove = cardMove;
               }
               else if(card.getValue() != 1 && card.getValue() !=13){
                   Card _bestMove = (Card)bestMove;
                   if(bestMove == null || card.getValue() > _bestMove.getValue()){
                       bestMove = card.getValue();
                   }
               }

            }
        }


       /* state = (State)gameControl.getState();
        gameControl.checkPossibleMoves();

        for(Card card : state.getMovableCardsTableau()){
            for(LinkedList<Card> tableauList : state.tableau)
            if(card.getValue() == 12 && card.getMovesToEmptySpace() != null){
                bestMove = card.getMovesToEmptySpace().peek();
            }
        }*/
        return bestMove;
    }


}
