package com.cdio.ss3000.DataLayer;

import java.util.LinkedList;

public class PointCalculator {

    /*
    Value of empty slot in tableu is -1
    Value of empty slot in foundation is -2
     */
    private Card checkMoves(LinkedList<LinkedList> listOfMoves){
        if(listOfMoves.isEmpty()) {
            return null;
        }else{
            for(LinkedList lists : listOfMoves){
                Card card = (Card)lists.peek();
                if(card.getPlacedInFoundation()){
                    return card;
                }
                //TODO: If it is not a move to tableu do something?
            }
        }
        return null;
    }

    public Card getMoveWithMostPoint(Card card){
        return new Card();
    }
}
