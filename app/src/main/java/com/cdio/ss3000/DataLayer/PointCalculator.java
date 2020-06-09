package com.cdio.ss3000.DataLayer;

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
            //Empty spot in Tableau
            if(move.getValue() == -1 && move.getSuit() == Suit.UNKNOWN){
                temp_points += 3;
            }
            //Empty spot in Foundation
            if(move.getValue() == -2 && move.getSuit() == Suit.UNKNOWN){
                temp_points += 5;
            }
            //Any other card in the tableau
            if(move.getValue() > 0 && move.getValue() < 14){
                temp_points += 2;
                //Add points for each underlying face down card
            }
            move.setPoints(temp_points);
        }
        Card newCard = null; //This will be used to save the final card to be returned
        int highestPoint = 0; //The current highest number of points
        for(int j = 0; j < card.getMoves().size(); j++){
            //Compares the points to the current highest if higher -> save as new highest
            if(card.getMoves().get(j).getPoints() > highestPoint){
                highestPoint = card.getMoves().get(j).getPoints();
                newCard = card.getMoves().get(j);
            }
        }

        return newCard;
    }

    public Card getBestMove(Card card){
        return checkMoves(card);
    }
}
