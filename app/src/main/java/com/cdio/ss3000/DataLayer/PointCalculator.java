package com.cdio.ss3000.DataLayer;

import java.util.LinkedList;

public class PointCalculator {

    /*
    Value of empty slot in tableu is -1
    Value of empty slot in foundation is -2
     */
    private Card checkMoves(Card card){
        int temp_points = 0;
        int i = -1;
        for(Card move : card.getMoves()){
            ++i;
            //Tableu
            if(move.getValue() == -1 && move.getSuit() == Suit.UNKNOWN){
                temp_points += 3;
            }
            //Foundation
            if(move.getValue() == -2 && move.getSuit() == Suit.UNKNOWN){
                temp_points += 5;
            }
            if(move.getValue() > 0 && move.getValue() < 14){
                temp_points += 2;
                //Add points for each underlying face down card
            }
            move.setPoints(temp_points);
        }
        Card newCard = null;
        int highestPoint = 0;
        for(int j = 0; j < card.getMoves().size(); j++){
            if(card.getMoves().get(j).getPoints() > highestPoint){
                highestPoint = card.getMoves().get(j).getPoints();
                newCard = card.getMoves().get(j);
            }
        }

        return newCard;
    }

    public Card getMoveWithMostPoint(Card card){
        return new Card();
    }
}
