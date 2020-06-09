package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;

public class PointCalculator {
    private final int ACE_POINTS = 100;
    private final int TABLEAU_EMPTY_POINTS = 3;
    private final int TABLEAU_POINTS = 2;
    private final int FOUNDATION_POINTS = 10;
    private final int ACE = 1;
    private final int KING = 13;

    private Card checkMoves(Card card){
        int temp_points = 0;
        //If card is an ace
        Card ace = checkAce(card);
        if(ace != null){
            return ace;
        }
        //Do all other moves
        Card newCard = checkCards(card);
        //Remove all previous moves from the supplied card
        card.clearMoves();
        //Save the new card (best move) to the list of moves (as the only one)
        ArrayList<Card> moves = new ArrayList<>();
        moves.add(newCard);
        //Add the list of moves to the supplied card
        card.addMove(moves);
        //Return the supplied card with the single best move
        return card;
    }

    private Card checkCards(Card card){
        int temp_points = 0;
        for(ArrayList<Card> move : card.getMoves()){
            //Empty spot in Tableau
            if(move.isEmpty() && card.getValue() == KING){
                temp_points += TABLEAU_EMPTY_POINTS;
            }
            //This will be a move to foundation
            if(move.get(move.size()).getSuit() == card.getSuit()){
                temp_points += FOUNDATION_POINTS;
            }

            //Any other card in the tableau
            if(move.get(move.size()-1).getValue() > 0 && move.get(move.size()-1).getValue() < 14){
                temp_points += TABLEAU_POINTS;
                //Add points for each underlying face down card
            }
            move.get(move.size()-1).setPoints(temp_points);
        }

        int highestPoint = 0;
        Card newCard = new Card();
        for(int j = 0; j < card.getMoves().size(); j++){
            Card currentCard = ((Card)card.getMoves().get(j).get(card.getMoves().size()-1));//Get the current card
            int cardPoints = currentCard.getPoints();//Get points from a card
            //Compares the points to the current highest if higher -> save as new highest
            if(cardPoints > highestPoint){
                highestPoint = cardPoints;
                newCard = currentCard;
            }
        }
        return newCard;
    }

    private Card checkAce(Card card){
        if(card.getValue() == ACE && !card.getPlacedInFoundation()){
            card.setPoints(ACE_POINTS);
            return card;
        }else{
            return null;
        }
    }

    public Card getBestMove(Card card){
        return checkMoves(card);
    }
}
