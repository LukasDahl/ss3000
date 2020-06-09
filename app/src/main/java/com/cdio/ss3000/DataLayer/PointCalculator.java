package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;

public class PointCalculator {

    /*
    Value of empty slot in tableu is -1
    Value of empty slot in foundation is -2
     */
    private Card checkMoves(Card card){
        int temp_points = 0;
        //If card is an ace
        if(card.getValue() == 1 && !card.getPlacedInFoundation()){
            temp_points += 10;
            card.setPoints(temp_points);
        }
        //Do all other moves
        for(ArrayList<Card> move : card.getMoves()){
            //Empty spot in Tableau
            if(move.isEmpty() && card.getValue() == 13){
                temp_points += 3;
            }
            //This will be a move to foundation
            if(move.get(move.size()-1).getSuit() == card.getSuit()){
                temp_points += 10;
            }

            //Any other card in the tableau
            if(move.get(move.size()-1).getValue() > 0 && move.get(move.size()-1).getValue() < 14){
                temp_points += 2;
                //Add points for each underlying face down card
            }
            move.get(move.size()-1).setPoints(temp_points);
        }
        Card newCard = null; //This will be used to save the final card to be returned
        int highestPoint = 0; //The current highest number of points
        for(int j = 0; j < card.getMoves().size(); j++){
            Card currentCard = ((Card)card.getMoves().get(j).get(card.getMoves().size()-1));//Get the current card
            int cardPoints = currentCard.getPoints();//Get points from a card
            //Compares the points to the current highest if higher -> save as new highest
            if(cardPoints > highestPoint){
                highestPoint = cardPoints;
                newCard = currentCard;
            }
            if(highestPoint < card.getPoints()){
                highestPoint = card.getPoints();
                newCard = card;
            }
        }
        //Remove all previous moves from the supplied card
        card.clearMoves();
        //Save the new card (bestmove) to the list of moves (as the only one)
        ArrayList<Card> moves = new ArrayList<>();
        moves.add(newCard);
        //Add the list of moves to the supplied card
        card.addMove(moves);
        //Return the supplied card with the single best move
        return card;
    }

    public Card getBestMove(Card card){
        return checkMoves(card);
    }
}
